# Motivation #

The purpose of the rocket.selection package is to expose in a browser independent manner the ability to work with selections. A selection is what happens when the user of a web page drags the mouse over some text with the browser responding by showing the selection  with reversed colours.

# The solution #
The selection module provides a singleton which in turn may be used to manipulate or discover more about a selection that exists on the current page.

Most of the methods are self explanatory.

Global selection methods (they always work).
| **method** | **comments** |
|:-----------|:-------------|
| Selection.getSelection() | Fetches the Selection singleton. |
| Selection.disableTextSelection() | Disable text selection for the entire document. |
| Selection.disableTextSelection(Element) | Disable text selection for all children of the given elements. |
| Selection.enableTextSelection() | Enable text selection for the entire document. |
| Selection.enableTextSelection(Element) | Enable text selection for all children of the given elements. |
| Selection.clearAnySelectedText() | Clears any selection |

The table below lists functionality available upon valid selection.
| **method** | **comments** |
|:-----------|:-------------|
| isEmpty()  | Will return true if no selection is present |
| clear()    | Clears the current selection |
| extract()  | Removes or **cuts** the selected content from the visible document and makes it the child of a new element. |
| delete()   | Cuts the selection from the document |
| surround( Element element ) | Inserts the given element into the dom so that it is a child of the given element and yet contains the selected area. |
| getStart() | Get the start of a selection |
| setStart(SelectionEndPoint) | Positions the start of a selection |
| getEnd()   | Get the end of a selection |
| setEnd(SelectionEndPoint) | Positions the end of a selection |

  * `rocket.selection.client.SelectionEndPoint` - The start and end of a selection (if one
is present) are defined by a text node and an character offset within that node.

## Potential use cases ##
Most of the selection functionality can be used to write a simple rich text (html) editor.
  * The Selection.delete() method may be used to delete whatever the user has selected.
  * The Selection.surround(Element) may be used to apply a style to whatever has selected after updating the Element's inline style.
  * The Selection.extract() may be used to cut a selection. Pasting merely involves adding the returned element to somewhere else in the dom.

# Getting started #

After downloading and unzipping the download the last step required is to inherit the `rocket.selection.Selecton` module.

```
<inherit name="Rocket.selection.Selection" />
```

# Function tests #

For further examples refer to the unit demo. The demo is interactive and prompts the user to perform or verify each of the capabilities.
> `rocket.selection.test.selection.client.SelectionTest`