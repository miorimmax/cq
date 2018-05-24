# cq - jq for EDN data

`cq` provides the same functionality `jq` does for JSON, allowing one to interact with EDN files by performing queries and manipulating input/output as necessary.

## Requirements

You will need Clojure 1.9 with `clj`. See [the Clojure documentation](https://clojure.org/guides/deps_and_cli) for details.

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

```bash
# Takes the input of #string tag and applies the function (partial str) on it
cq --data-reader '[string (partial str)]' < file.edn
```
