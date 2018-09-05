# cq - jq for EDN data

`cq` provides the same functionality `jq` does for JSON, allowing one to interact with EDN files by performing queries and manipulating input/output as necessary.

## Requirements

You will need Clojure 1.9 with `clj`. On a macOS, you can install it with brew:

```
brew install clojure
```

See [the Clojure documentation](https://clojure.org/guides/deps_and_cli) for details.

## Installation

Clone this repository and create a symbolic link of `cq` somewhere in your `$PATH`, for example:

```bash
ln -sv cq /usr/local/bin/cq
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

### Providing a custom data reader

Edit `cq.clj` and change the `default-reader-fn` implementation.

You can also extend the map passed to `edn/read` to add more custom readers.
