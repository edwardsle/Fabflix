let params = new URLSearchParams(location.search.slice(1));

	var movieListElem = new Vue({
	    el: '#movie-list',
	    data : {
	    	movieList : '',
	    	ready: false,
	    	params : {
	    		genreId : params.get('genreId'),
	    		letter : params.get('letter'),
	    		sortBy : params.get('sortBy'),
	    		sort : params.get('sort'),
	    		year : params.get('year'),
	    		title : params.get('title'),
	    		director : params.get('director'),
	    		star : params.get('star'),
	    		page : (params.get('page') == null ? 1 : parseInt(params.get('page'))),
	    		maxPage : 0,
	    		limit : (params.get('limit') == null ? 20 : parseInt(params.get('limit')))
	    	}
	    },
	    methods : {
	    	addCartFromMovieComponent(movieId){
	    		cartBridgeAdd(movieId);
	    	},
	    	nextPage() {
	    		if (this.params.page < this.params.maxPage)
	    			this.params.page++;
	    	},
	    	prevPage() {
	    		if (this.params.page > 1)
	    			this.params.page--;
	    	},
	    	offsetPage(offset) {
	    		osPage = offset-3+this.params.page;
	    		if (this.params.page <= 3)
	    			osPage = offset;
	    		else if (this.params.page >= (this.params.maxPage - 2))
	    			osPage = this.params.maxPage+offset-5;
	    		if (osPage > 0 && osPage <= this.params.maxPage)
	    			return (osPage);
	    		else
	    			return '.';
	    	},
	    	changeSort(sortBy) {
	    		this.params.sortBy = sortBy;
	    		if (this.params.sort == null || this.params.sort == 'asc')
	    			this.params.sort = 'desc';
	    		else
	    			this.params.sort = 'asc';
	    		this.load();
	    	},
	    	load() {
	    		var path = '/project5/api/movie';
		    	axios
		    	.get(path, {params : this.params})
		    	.then((response) => {
		    		this.movieList = response.data.data;
		    		this.params.maxPage = response.data.maxPage;
		    		this.ready = true;
		    	});
	    	}
	    },
	    mounted() {
	    	this.load();
	    }
	})