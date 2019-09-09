<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="project1.Movie" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Top Rated Movies - Fablix</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>
<style>
/* Background Color */
body {background-color: #e5e5e5;}
</style>
	<!-- Nagivation Bar -->
	<nav class="navbar navbar-inverse navbar-fixed-top">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">Fablix</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
      	<li><a href="#">Home</a></li>
        <li class="active"><a href="#">Top Rated <span class="sr-only">(current)</span></a></li>
        <li><a href="#">Most Popular</a></li>
        <li><a href="#">Featured</a></li>
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Categories <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="#">Action</a></li>
            <li><a href="#">Horror</a></li>
            <li><a href="#">Comedy</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="#">Separated link</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="#">One more separated link</a></li>
          </ul>
        </li>
      </ul>
      <form class="navbar-form navbar-left">
        <div class="form-group">
          <input type="text" class="form-control" placeholder="Search Movies..">
        </div>
        <button type="submit" class="btn btn-default">Search</button>
      </form>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#">About</a></li>
        <li><a href="#">Contact Us</a></li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>
	<div class="p-1">
	    <div class="container text-center">
	          <h1 class="display-1" style="margin-top:75px;">Top Rated Movies</h1>
	    </div>
	</div>
	
	<!-- Top Rated Movies List -->
	<div class="container">
	          <div class="list-group">
	         <c:forEach items="${movies}" var="movie" varStatus="mvStatus">
	
	              <div class="d-flex w-100 justify-content-between">
	              	<div class="panel panel-primary">
	              		<div class="panel-heading">
	                		<h2 class="panel-title">${mvStatus.count}. ${movie.title}</h2>
	                	</div>
	                	<div class="panel-body">
			                <div class="row">
			                <div class="col-sm-2 mb-1">Year</div>
						    <div class="col-sm-10 mb-1">${movie.year}</div>
			          		</div>
			                <div class="row">
			                <div class="col-sm-2 mb-1">Director</div>
						    <div class="col-sm-10 mb-1">${movie.director}</div>
			                </div>
			                <div class="row">
			                <div class="col-sm-2 mb-1">Stars</div>
						    <div class="col-sm-10 mb-1">
						    <c:forEach items="${movie.stars}" var="star" varStatus="status">
						    ${star.name}<c:if test="${not status.last}">,</c:if>
						    </c:forEach>
						    </div>
			                </div>
			                <div class="row">
			                <div class="col-sm-2 mb-1">Genres</div>
						    <div class="col-sm-10 mb-1">
						    <c:forEach items="${movie.genres}" var="genre" varStatus="status">
						    ${genre.name}<c:if test="${not status.last}">,</c:if>
						    </c:forEach>
						    </div>
			                </div>
			                <div class="panel-foot">
			                <div class="row">
			                <div class="col-sm-2 mb-1">Rating</div>
						    <div class="col-sm-10 mb-1">${movie.rating}</div>
						   </div>
						   </div>
						 </div>
		            </div>
		         </div>
	          		
	          		
	         </c:forEach>
	          </div>
	</div>
	<!-- Pagination -->
	<style>
.pagination {
    display: inline-block;
}

.pagination a {
    color: black;
    float: left;
    padding: 8px 16px;
    text-decoration: none;
    transition: background-color .3s;
}

.pagination a.active {
    background-color: #8c8c8c;
    color: white;
}

.pagination a:hover:not(.active) {background-color: #6d6d6d;}
</style>
</head>
<body>
<div class="text-center">
	<ul class="pagination">
	  <a href="#">&laquo;</a>
	  <a href="#" class="active">1</a>
	  <a href="#">2</a>
	  <a href="#">3</a>
	  <a href="#">4</a>
	  <a href="#">5</a>
	  <a href="#">6</a>
	  <a href="#">&raquo;</a>
 	 </ul>
</div>
</body>
</html>