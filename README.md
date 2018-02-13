## Why this fork?

I am building a Cypher kernel for Jupyter notebooks, see https://github.com/HelgeCPH/cypher_kernel.

To allow for parsing of the REPL output into visualizations, I need a bit more of information on graph entities. In particular, each entity needs to carry it's own `id` and each relation needs to carry the source and target nodes. That is, this modified `OutputFormatter` generates in `verbose` mode output as in the following for the corresponding model and query:

```cypher-shell
CREATE ( bike:Bike { weight: 10 } ) 
CREATE ( frontWheel:Wheel { spokes: 3 } ) 
CREATE ( backWheel:Wheel { spokes: 32 } ) 
CREATE p1 = (bike)-[:HAS { position: 1 } ]->(frontWheel) 
CREATE p2 = (bike)-[:HAS { position: 2 } ]->(backWheel) 
RETURN bike, p1, p2
```

```cypher
MATCH (a)-[b:HAS]-(c) RETURN a, b, c;
```

```
neo4j> MATCH (a)-[b:HAS]-(c) RETURN a, b, c;
+-----------------------------------------------------------------------------------------------------------+
| a                               | b                                     | c                               |
+-----------------------------------------------------------------------------------------------------------+
| (:Bike {weight: 10, _id_: 58})  | [:HAS {_id_: 36, position: 2}[58>60]] | (:Wheel {spokes: 32, _id_: 60}) |
| (:Bike {weight: 10, _id_: 58})  | [:HAS {_id_: 35, position: 1}[58>59]] | (:Wheel {spokes: 3, _id_: 59})  |
| (:Wheel {spokes: 3, _id_: 59})  | [:HAS {_id_: 35, position: 1}[58>59]] | (:Bike {weight: 10, _id_: 58})  |
| (:Wheel {spokes: 32, _id_: 60}) | [:HAS {_id_: 36, position: 2}[58>60]] | (:Bike {weight: 10, _id_: 58})  |
+-----------------------------------------------------------------------------------------------------------+

4 rows available after 1 ms, consumed after another 0 ms
```


The original output would look like:

```
neo4j> MATCH (a)-[b:HAS]-(c) RETURN a, b, c;
+----------------------------------------------------------------------+
| a                     | b                    | c                     |
+----------------------------------------------------------------------+
| (:Bike {weight: 10})  | [:HAS {position: 2}] | (:Wheel {spokes: 32}) |
| (:Bike {weight: 10})  | [:HAS {position: 1}] | (:Wheel {spokes: 3})  |
| (:Wheel {spokes: 3})  | [:HAS {position: 1}] | (:Bike {weight: 10})  |
| (:Wheel {spokes: 32}) | [:HAS {position: 2}] | (:Bike {weight: 10})  |
+----------------------------------------------------------------------+

4 rows available after 0 ms, consumed after another 1 ms
```

## How to build

Use `make help` (`gradlew tasks`) to list possible tasks. But you
probably want either

-  `make build` (`gradlew installDist`) which will build an
   uber-jar and runnable script for you at
   `cypher-shell/build/install/cypher-shell`

- `make zip` which builds an uber-jar with runnable script and
   packages it up for you as: `out/cypher-shell.zip` Note that this
   will run a test on the script which requires a instance of neo4j
   (see Integration tests below).

- `make untested-zip` which builds the same zip file but doesn't test
  it. It will be placed in `tmp/cypher-shell.zip`.

## How to run, the fast way

This clears any previously known neo4j hosts, starts a throw-away
instance of neo4j, and connects to it.

```sh
rm -rf ~/.neo4j/known_hosts
docker run --detach -p 7687:7687 -e NEO4J_AUTH=none neo4j:3.0
make run
```

## How to build packages

Packages require you to have `pandoc` available. It should be
available in your local package manager.

Then just do

```
make debian rpm
```

To test the packages you need to have Docker installed:

```
make debian-test rpm-test
```

To get the versions correct when building packages you can override
some variables, for example with:

```
make debian pkgversion=2
```

See `make info` for a list of variables and what the results will be.

## Development

### Integration tests

#### Pre Requisites for running integration tests

Neo4j server with bolt driver configured.

If authentication is required, it is assumed to be username `neo4j`
and password `neo`.

#### To run

Integration tests are usually skipped when you run `make test`
(`gradlew test`)

Use `make integration-test` (`gradlew integrationTest`) to
specifically run integration tests.

#### How to run the fast way

This clears any previously known neo4j hosts, starts a throw-away
instance of neo4j, and runs the integration tests against it.

```sh
rm -rf ~/.neo4j/known_hosts
docker run --detach -p 7687:7687 -e NEO4J_AUTH=none neo4j:3.0
make integration-test
```
