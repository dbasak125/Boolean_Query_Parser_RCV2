# Boolean_Query_Parser_RCV2
Boolean Query Parser on Reuters RCV2 Corpora

Description:
- Create an In-Memory index of the Reuters RCV2 Corpus
- Design a boolean query processor using TAAT/DAAT processing schemes
- Optimize search performance on In-Memory index using techniques like: skip-pointers and sorted DF/IDF parsing

Requires: lucene-core-6.2.1.jar

$ java -jar dbasak_project2.jar index output.txt input.txt

Arguments:
- index:      Path of folder containig Lucene Indexes of the RCV2 corpus.
- output.txt: Path to the search result output file 
- input.txt:  Path to file containig search query. search query terms are separated by 1 whitespace.

*Sample files are included in jar/.
