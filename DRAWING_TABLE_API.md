# Drawing Table API

This generic API exposes 2D drawing tables from the active Creo drawing through
J-Link / Creo Java API table objects. It contains no customer-specific table
names, filters, mappings, or business rules.

Creoson-native request form:

```json
{
  "command": "drawing",
  "function": "table_list",
  "data": {}
}
```

Compact request form is also accepted:

```json
{
  "command": "drawing.table.list"
}
```

`tableIndex` is 0-based. Row and column parameters use Creo/J-Link native
1-based indexing. If a Creo/J-Link release does not expose an operation, Creoson
returns a normal error response whose `status.message` starts with
`not_supported`.

## Implementation Notes

This API is implemented with documented Creo Object Toolkit Java / J-Link table
APIs:

- `pfcTable.TableOwner.ListTables`, `CreateTable`, `DeleteTable`, and
  `UpdateTables`
- `pfcTable.Table.GetRowCount`, `GetColumnCount`, `GetInfo`, `GetText`,
  `SetText`, `InsertRow`, `InsertColumn`, `DeleteRow`, `DeleteColumn`,
  `MergeRegion`, `SubdivideRegion`, and `MoveSegment`
- `pfcTable.pfcTable.TableCell_Create`, `TableCreateInstructions_Create`, and
  `ColumnCreateOption_Create`
- `wfcTable.WTable.GetColumnWidth`, `GetRowHeight`, `SetColumnWidth`, and
  `SetRowHeight` where the running Creo/J-Link API exposes WTable methods

`drawing.table.create` creates a simple table using generic defaults:
`TABLESIZE_BY_NUM_CHARS`, row height `1.0`, column width `10.0`, and left
column justification.

`drawing.table.insert_rows` passes row height in character units, matching the
`pfcTable.Table.InsertRow` requirement.

## Commands

### drawing.table.list

```json
{ "command": "drawing.table.list" }
```

```json
{
  "status": { "error": false },
  "data": {
    "tables": [
      { "tableIndex": 0, "rows": 2, "columns": 3, "location": { "x": 10.0, "y": 20.0, "z": 0.0 } }
    ]
  }
}
```

### drawing.table.get_info

```json
{ "command": "drawing.table.get_info", "tableIndex": 0 }
```

```json
{
  "status": { "error": false },
  "data": {
    "tableIndex": 0,
    "rows": 2,
    "columns": 2,
    "cells": [
      { "row": 1, "column": 1, "text": "Name" },
      { "row": 1, "column": 2, "text": "Value" }
    ]
  }
}
```

### drawing.table.get_cell

```json
{ "command": "drawing.table.get_cell", "tableIndex": 0, "row": 1, "column": 2 }
```

```json
{ "status": { "error": false }, "data": { "row": 1, "column": 2, "text": "Value", "value": "Value" } }
```

### drawing.table.set_cell

```json
{ "command": "drawing.table.set_cell", "tableIndex": 0, "row": 2, "column": 2, "value": "A-123" }
```

```json
{ "status": { "error": false } }
```

### drawing.table.get_range

```json
{ "command": "drawing.table.get_range", "tableIndex": 0, "startRow": 1, "startColumn": 1, "endRow": 2, "endColumn": 2 }
```

```json
{ "status": { "error": false }, "data": { "values": [["Name", "Value"], ["Part", "A-123"]] } }
```

### drawing.table.set_range

```json
{ "command": "drawing.table.set_range", "tableIndex": 0, "startRow": 2, "startColumn": 1, "values": [["Part", "A-123"], ["Rev", "B"]] }
```

```json
{ "status": { "error": false } }
```

### drawing.table.insert_rows

```json
{ "command": "drawing.table.insert_rows", "tableIndex": 0, "atRow": 4, "count": 3, "position": "after" }
```

```json
{ "status": { "error": false } }
```

### drawing.table.delete_rows

```json
{ "command": "drawing.table.delete_rows", "tableIndex": 0, "rows": [4, 5, 6] }
```

Rows are deleted from highest index to lowest index.

```json
{ "status": { "error": false } }
```

### drawing.table.insert_columns

```json
{ "command": "drawing.table.insert_columns", "tableIndex": 0, "atColumn": 2, "count": 1, "position": "before" }
```

```json
{ "status": { "error": false } }
```

### drawing.table.delete_columns

```json
{ "command": "drawing.table.delete_columns", "tableIndex": 0, "columns": [3, 4] }
```

Columns are deleted from highest index to lowest index.

```json
{ "status": { "error": false } }
```

### drawing.table.clear_cell

```json
{ "command": "drawing.table.clear_cell", "tableIndex": 0, "row": 2, "column": 2 }
```

```json
{ "status": { "error": false } }
```

### drawing.table.clear_range

```json
{ "command": "drawing.table.clear_range", "tableIndex": 0, "startRow": 2, "startColumn": 1, "endRow": 3, "endColumn": 2 }
```

```json
{ "status": { "error": false } }
```

### drawing.table.move

```json
{ "command": "drawing.table.move", "tableIndex": 0, "point": { "x": 10.0, "y": 20.0, "z": 0.0 } }
```

```json
{ "status": { "error": false } }
```

### drawing.table.set_column_width

```json
{ "command": "drawing.table.set_column_width", "tableIndex": 0, "column": 2, "width": 25.0 }
```

```json
{ "status": { "error": false } }
```

### drawing.table.set_row_height

```json
{ "command": "drawing.table.set_row_height", "tableIndex": 0, "row": 2, "height": 8.0 }
```

```json
{ "status": { "error": false } }
```

### drawing.table.merge_cells

```json
{ "command": "drawing.table.merge_cells", "tableIndex": 0, "startRow": 1, "startColumn": 1, "endRow": 1, "endColumn": 2 }
```

```json
{ "status": { "error": false } }
```

### drawing.table.unmerge_cells

```json
{ "command": "drawing.table.unmerge_cells", "tableIndex": 0, "startRow": 1, "startColumn": 1, "endRow": 1, "endColumn": 2 }
```

```json
{ "status": { "error": false } }
```

### drawing.table.create

```json
{ "command": "drawing.table.create", "rows": 2, "columns": 2, "point": { "x": 10.0, "y": 20.0, "z": 0.0 }, "values": [["Name", "Value"], ["Part", "A-123"]] }
```

If `point` is omitted, the origin defaults to `{ "x": 0.0, "y": 0.0,
"z": 0.0 }`. Initial `values` must fit within the requested row and column
count.

```json
{ "status": { "error": false } }
```

### drawing.table.delete

```json
{ "command": "drawing.table.delete", "tableIndex": 0 }
```

```json
{ "status": { "error": false } }
```

### drawing.table.regenerate

```json
{ "command": "drawing.table.regenerate" }
```

```json
{ "status": { "error": false } }
```

## Manual Test Instructions

1. Start Creo and Creoson, open a drawing with at least one simple 2D table, and make it the active model.
2. Call `drawing.table.list`; verify the returned `rows` and `columns` match the drawing.
3. Call `drawing.table.get_info` for `tableIndex: 0`; verify non-empty cells are returned and empty cells return `""`.
4. Call `drawing.table.set_cell`, then `drawing.table.get_cell` for the same cell.
5. Call `drawing.table.set_range`, then `drawing.table.get_range` for the same rectangle.
6. On a disposable drawing copy, test insert/delete rows and columns. Verify deletion order is safe for `[4, 5, 6]`.
7. On a disposable drawing copy, call `drawing.table.create` and verify the new simple table appears with optional initial values.
8. Test optional operations (`move`, sizing, merge, unmerge, delete`) on the target Creo version. Accept `not_supported` where the native J-Link API does not expose the operation.
9. Call `drawing.table.regenerate` and visually confirm the drawing table display is updated.
