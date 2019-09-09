XML Parsing Optimization

1) We implemented the XML parsing into database using SAX parser instead of DOM parser since it is an even-based parser and extracts information as the document is being read (more suitable for large XML as it doesn't require much memory) while DOM parser loads the entire document into memory and then traverses the tree to find information.

2) We use preparedStatement instead of just Statement, which saved us some time

3) We set autocommit to 0 (turn it off) 

4) We use HashMap