# Project Overview: 
EasyShop is an application that aims at simplifying the daily task of acquiring items from retail stores. Though initially the software idea was different, it morphed over the course of the semester into what it is now. Spanning many useful features, that can be used by users of any age, it provides a tool to seek and scour any store and find what is desired. The target Audience of our application is mainly those who need help finding, choosing and requiring additional information about items; store owners and/or employees that want to dynamically add offers or change prices on products without having to change numerous labels, as well as software developers that have a similar idea in mind and want to build on a solid starting point; hence the application is hosted on github and will be open source. 
 
## Resources used: 
  * Android Studio 
  * Gradle
  * Google mobile vision API - multi tracker API 
  * AWS dynamodb 
  * AWS lambda/gateway 
  * FireBase 
  * Sprinboot framework (for REST requests) 
  * Google Location Services 
  * OpenGL/OpenCV (first iteration) 
  * Github 
  * Zbar/ZXing APIs 
  * Unity
  * Vuforia
 
## Languages used:
 * C/C++
 * Java
 * Javascript
 * Nodejs
 * Python 
 
 
## Initial Idea - Problems and Solutions 
The initial conception of the application was a hands-on tool that can be used with Google-Cardboard to provide an AR experience while shopping for items, such that prices are displayed with big, bright, 3 dimensional number above their respective barcodes as well as advertisements that pop up here and there inside the store to promote items. That soon turned out to be not applicable, Google cardboard (which is one of the affordable VR/AR head sets) turned out to have a too narrow field of view to turn the concept into reality. We stuck to the barcode detection feature, because it is a must have, and no other application on the app store or google play does it nearly as well. The rest of the application evolved down the road, where gps location and store selection was added as well as a shopping list that acts as a quasi reminder. 
 
## Initial Attempts: 
Initially we created a c++ application that tracks and detects barcodes. It was patched up by using OpenCV libraries that enable the usage of the camera and fetching of the video feed frame by frame. Those frames were pipelined into the Zbar API (based on OpenGL) that detects barcodes in Images using visual recognition, and image feature detection. In simpler terms it does not actually know that it is detecting a barcode, It's just altering the image until the barcode area stands out as a white spot surrounded by black terrain and draws a bounding box along the edges.  
This provided an initial solution (2-3 weeks) for what we aimed to do, but it was very inconsistent and detection was unreliable. And porting it posed an immense problem, due to having to compile native c++ libraries on an android device. It could have been done using JNI, but after a few attempts that was dropped and we moved on to a different solution provided by Google Mobile Vision: a library that allows the detection of almost everything. 
 
## Mobile Vision 
Integrating the [Mobile Vision API](https://github.com/googlesamples/android-vision/tree/master/visionSamples/barcode-reader) happened seamlessly.

### Performance
The barcode tracker proved to be an over-performer; during battery consumption tests it showed that it was very lightweight and not as straining on the CPU and Memory as Zbar and Zxing were, which in turn showed a heavy consumption of CPU and Memory with wide fluctuations. 

### How it works
Based on a factory design pattern, it creates a tracker object upon detecting a new barcode item in the frame presented, which then draws a bounding box around the barcode and follows it even when the camera is moved around, this is achieved by a graphic overlay that is drawn over the frame and continuously refreshed. Using trained neural networks to detect features in images it is much faster and more reliable than Zbar that was previously used. 
 
## Database 
### Population
After research, it was evident that there is no global repository that holds every single barcode along with item's information. Albeit there being some services that provide look-up functions for barcodes, and that actually provide a good number, most of the local items in Lebanon are unidentifiable by them. We decided to contact some of the major retail stores in Lebanon such as  Spinneys and Monoprix such as to acquire their databases, but that proved to be a vain attempt. Ultimately we decided to slowly fill our own database.

### Backend Function - REST
AWS Lambda is a splendid tool for that purpose, where we can programmatically switch results in and out. For example, if an item is not detected in our database, the function detects this and switches to a barcode lookup function from one of the global repositories and hence try there.

This is done by a consuming a restful webservice, using the springboot framework, the barcode trakcer creates an ASYNCTask that fires a web request to our Lambda function (through the API gateway), which in turn contacts dynamodb and queries the requested rows and returns them in a callback. Once those results arrive at the application the UI updates and displays the information.

On the front end the request is instantiated as such:
```java
AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // The connection URL
                String url = "https://frysht0l68.execute-api.us-east-2.amazonaws.com/prod/get?type=barcode&barcode=" + barcodeNumber + "&letter=0";

                // Create a new RestTemplate instance
                RestTemplate restTemplate = new RestTemplate();

                // Add the String message converter
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                // Make the HTTP GET request, marshaling the response to a String
                String result = restTemplate.getForObject(url, String.class, "Android");

                try {
                    JSONObject jsonObj = new JSONObject(result);
                    code = new Code(new JSONObject(result));
                } catch (JSONException e) {
                    Log.d("Exception", "code creation from json object");
                }

            }
 });
```

And the response:
```nodejs
exports.handle = function(e,ctx,callback) {
    if(e.type == "letter"){
        var params = {
            TableName: 'BarNameDummy',
            ProjectionExpression: 'BarName, Barcode, Price, Description',
            FilterExpression: 
                'begins_with(BarName, :letter)',
            ExpressionAttributeValues: {
                ':letter': e.letter
            }
        };  
      
        dynamodb.scan(params, function(err, data){
		          if(err){
			             callback(err, null);
		          }else{
			             callback(null, data.Items);
		          }
	       });
    }else if(e.type == "barcode"){
        var params2 = {
            TableName: 'BarNameDummy',
            ProjectionExpression: 'BarName, Barcode, Price, Description',
            FilterExpression: 
                'begins_with(Barcode, :letter)',
            ExpressionAttributeValues: {
                ':letter': e.barcode
            }
        }; 
        
        dynamodb.scan(params2, function(err, data){
		          if(err){
			             callback(err, null);
		          }else{
			             callback(null, data.Items[0]);
	    	      }
       });
    }
};
```
 
## Current Features 
* Barcode Detection 
* GPS location 
* Store selection 
* Item selection 
* Interactive Shopping List  
