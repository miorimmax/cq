# cq - jq for EDN data

`cq` provides the same functionality `jq` does for JSON, allowing one to interact with EDN files by performing queries and manipulating input/output as necessary.

## Developing
### Requirements

You will need Clojure 1.9 with `clj`. On a macOS, you can install it with brew:

```
brew install clojure
```

See [the Clojure documentation](https://clojure.org/guides/deps_and_cli) for details.

And you will also need GraalVM, see [its documentation](https://www.graalvm.org/docs/getting-started/#install-graalvm) for details.

### Building

Clone this repository and run the `:native-image` alias to build the native binary:

```bash
clojure -A:native-image
```

Then, copy it to a location in your `$PATH`.

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
