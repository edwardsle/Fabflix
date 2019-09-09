	var user = new Vue({
	    el: '#user',
	    data : {
	    	id : null,
	    	firstname: null,
	    	lastname: null
	    },
	    methods : {
	    	handleLogout () {
	    		axios
		    	.post('/project2/api/logout')
		    	.then((response) => {
		    		window.location.href = '/project2/login.html';
		    	});
	    	}
	    	
	    },
	    mounted() {
	    	axios
	    	.get('/project2/api/me')
	    	.then((response) => {
	    		this.id = response.data.id;
	    		this.firstname = response.data.firstName;
	    		this.lastname = response.data.lastName;
	    	});
	    	}
	})