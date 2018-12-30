# cq - jq for EDN data

`cq` provides the same functionality `jq` does for JSON, allowing one to interact with EDN files by performing queries and manipulating input/output as necessary.

## Developing

### Requirements

You will need Clojure 1.9 with `clj`. On a macOS, you can install it with brew:

```
brew install clojure
```

See [the Clojure documentation](https://clojure.org/guides/deps_and_cli) for details.

### Building

Clone this repository and run the `:uberjar` alias to build the native binary:

```bash
clojure -A:uberjar
```

Then, make a symlink to `bin/cq` in a location in your `$PATH`, for example:

```bash
ln -sv $(pwd -P)/bin/cq /usr/local/bin
```

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

### Faster startup time

You can use something like [drip](https://github.com/ninjudd/drip) to speed up the JVM startup time.

After installing it, you can export `$CQ_JAVA_CMD` using `drip` and `cq` will use it automatically:

```bash
export CQ_JAVA_CMD=/usr/local/bin/drip
```
