# cq - command line tool to interact with EDN data/files

`cq` provides similar functionality that `jq` does for JSON: allowing one to interact with EDN files by performing queries and manipulating input/output as necessary using Clojure's syntax.

## Developing

### Building

Clone this repository and run `lein bin` to build a binary.

Then, copy the resulting binary a location in your `$PATH`.

## Usage

### Querying values

```bash
# You can provide a function and it will be applied to the entire EDN, printing its output in the end
cq '#(-> % :deep :nested :key)' < file.edn
```

```bash
# If you just want a single key, you can also use the :keyword form and it works just like Clojure
cq ':a-key' < file.edn
```

### Providing a custom default data reader

You can provide a custom default data reader with the `--default-reader-fn` (or `-d`, for short) option. For example:

```bash
cq --default-reader-fn '(fn [tag input] (format "Custom reader: tag %s, input %s" tag input))'
```
