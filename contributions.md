Hi there! You want to contribute to Clarango? Great, because we need some help! Here are a couple of notes to get you started.

###Tests

There are tests for most features in the tests folder. After you changed or added something, run `lein test` and see if there are no errors. Also make sure to add a test for whatever you add to Clarango. The tests are split up in three test namespaces, you may have to search where a certain feature is tested because the grouping is a little arbitrary.

While you are working on Clarango you may want to activate some verbose console output in order to debug. You can do that by setting the methods `http-debugging-activated?`, `type-output-activated?` and `console-output-activated?` output in the `utilities/http-utility` namespace to `true`.

###What is missing?

Here are a couple of features that Clarango is still missing (please remove when done):

* document batch requests (clj-http may give you a hard time doing that, let’s hope it will be easier with future versions)
* update the `graph` namespace to use [ArangoDBs new graph interface](https://docs.arangodb.com/HttpGharial/README.html) (this has been started with v0.6.0, but there is still a lot to do)
* sharding
* async functions (check out [travesedo](https://github.com/deusdat/travesedo) which already has implemented this)

###Contribute

If you have questions or just want to get in touch, email to `peter [dot] fessel [at] live [dot] de` or `edlich [at] gmail [dot] com`.

After your pull request, we will validate your contribution and depending on how much is changed, make a new release out of it or wait for a couple of more contributions.