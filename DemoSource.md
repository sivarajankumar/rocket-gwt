# Shows how to find the source for any of the packaged demos

# Introduction #
This page will attempt to help describe and highlight the process involved in mapping one of the interactive demos found in the download under the www directory.

# Example locating the Menu Widget source #

The demo directory  within the download.
```
www
	rocket.widget.test.menu.Menu
```


The source for the menu widgets
```
src
	rocket
		widget
			client
				menu
					Constants
					ContextMenu
					ContextMenuOpenEvent
					HorizontalMenuBar
					HorizontalMenuList
					Menu
					MenuItem
					MenuList
					MenuListener
					MenuListenerCollection
					MenuListOpenDirection
					MenuOpenEvent
					MenuSpacer
					MenuWidget
					SubMenuItem
					VerticalMenuBar
					VerticalMenuList
```

The source for accompanying test/demo.

**_Within Eclipse the demo can be easily be found using OpenType (CTRL+SHIFT+T) and typing MenuTest._**

```
src
	rocket
		widget
			test
				menu
					public
						Menu.html
					Menu.gwt.xml
					client
						MenuTest.java
```