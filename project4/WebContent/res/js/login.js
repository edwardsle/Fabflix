new Vue({
    el: '#login-form',
    data : {
    	form : {
    		email: '',
            password: ''
    	},
    	info : ''
    },
    methods : {
    	handleSubmit() {
    		axios
    		.post('/project4/api/login', this.form)
    		.then((response) => {
    			this.info = response.data;
    			if (this.info.status == 'success')
    				window.location.href = '/project4/index.html';
    		});
    	}
    }
})