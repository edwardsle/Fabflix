PROJECT(edth)

# Path for MySQL include directory
INCLUDE_DIRECTORIES("C:\Program Files\MySQL\MySQL Server 5.7\include")

ADD_DEFINITIONS("-DHAVE_DLOPEN")
ADD_LIBRARY(edth MODULE edth.c)
TARGET_LINK_LIBRARIES(edth wsock64)