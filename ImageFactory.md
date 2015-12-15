# A Factory for images with smart support for data urls wherever possible.

# Motivation #
The primary motivation is to make use of data urls as a way of creating images if the current browser supports them. There are several important consideration when creating data urls, including observing browser size limits, Opera for instance only accepts data uris that are less than 4100 bytes. On the other hand Internet Explorer does not support data uris at all.

# Goals #
  * Provide a factory which optimally creates images, using data urls wherever possible.

# ImageFactory #
The ImageFactory interface along with a variety of annotations provide the capability for a developer to create a factory. Depending on browser capabilities and other configuration data the images that are products of this factory can contain a mixture of data and server urls.

Another advantage is Image may be bound to actual files at compile time rather than waiting for images not to show up.

## Getting started ##
Import the `rocket.widget.Widget` module in your application.gwt.xml file.

```
	<inherits name="rocket.widget.Widget" />
```

## Defining an interface ##
The developer must create an interface that extends `rocket.widgets.client.ImageFactory`. THe interface will contain one or more getters with annotations that describe the location, and other options. The example below has two methods, the help icons being particularly small and a large auto photo. For the help icon it makes sense to use a data url when possible. However because its unlikely the author pic will be shown often it doesnt make sense to cache it and drastically increase the size and load time of the application.

```
package...

public interface SampleImageFactory extends rocket.widget.client.ImageFactory{

	/**
	 * @file help-icon.png
	 * @location local
	 * @serverRequest lazy
	 */
	Image createHelpIcon();

	/**
	 * @file author-pic.png
	 * @location server
	 * @serverRequest lazy
	 */
	Image createAuthorPic();
}
```

The table below describes the purpose and available values for each annotation.
| Annotation | Description | Mandatory |
|:-----------|:------------|:----------|
| file       | A location of the image as a class path resource. Relative paths are relative to the Interface package | yes       |
| location   | **local** - images use data urls wherever possible/supported. **server** - urls are always server urls, never data urls | yes       |
| serverRequest | Controls whether or not server images are loaded eagerly or lazily. **eager** - server images are prefetched. **lazy** - The iamge url is fetched when the first image is created. | yes       |

## Application code ##

The code below demonstrates how to create an concrete instance of the SampleImageFactory interface defined previous.

```
	//... within some method...

	// use deferred binding to get a realisation of the interface.	
	SampleImageFactory factory = (SampleImageFactory) GWT.create(SampleImageFactory.class);

	// create both images...	
	Image helpIcon = factory.createHelpIcon();
	Image authorPic = factory.createAuthorPic();

	// FF, Safari and Opera will print a data url (they start with data:)
	System.out.println( helpIcon.getUrl() );

	// All browsers will print a server uri.
	System.out.println( authorPic.getUrl() );
```
For further examples refer to the unit test and related demos.
  * `rocket.widget.test.imagefactory.client.ImageFactory`