new Vue({
    el: '#login-form',
    data : {
    	form : {
    		email: '',
            password: '',
    	},
    	info : ''
    },
    methods : {
    	handleSubmit() {
    		axios
    		.post('/project2/api/login', this.form)
    		.then((response) => {
    			this.info = response.data;
    			if (this.info.status == 'success')
    				window.location.href = '/project2/index.html';
    		});
    	}
    }
})