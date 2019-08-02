# cq - command line tool to interact with EDN data/files

`cq` provides similar functionality that `jq` does for JSON: allowing one to interact with EDN files by performing queries and manipulating input/output as necessary using Clojure's syntax.

## Developing

### Building

Clone this repository and run `lein bin` to build a binary.

Then, copy the resulting binary a location in your `$PATH`, for example:

```bash
lein bin
cp target/cq /usr/local/bin/cq
```

## Usage

### Querying values

You can provide a Clojure function:
```bash
cq '(fn [data] (get-in data [:some :deep :nested :key]))' file.edn
```

You can get a single key from the map (which is just another Clojure function):
```bash
cq ':a-key' file.edn
```

You can also put your filter function in a file, using `--from-file` (or `-f`, for short):
```bash
cq -f very-complex.clj file.edn
```

### Providing a custom default data reader

You can provide a custom default data reader with the `--default-reader-fn` (or `-d`, for short) option. For example:

```bash
cq -d '(fn [tag input] (format "Custom reader: tag %s, input %s" tag input))'
```

### Providing custom data readers

You can also provide data readers for specific tags, like your own reader for `#inst`, for example.

To do so, create a `data_readers.clj` file and and use the `--data-readers` option (or `-r`, for short):

```bash
cq -r data_readers.clj identity file.edn
```
