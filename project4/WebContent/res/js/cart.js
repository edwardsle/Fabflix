	function cartBridgeAdd(movieId) {
		cartElem.add(movieId);
	}
	var cartElem = new Vue({
		el: "#cart",
		data : {
			cart : null,
			showConfirm : false,
			showCart : true
		},
		methods: {
			add(id) {
				console.log(id);
				this.update({movieId : id, method : 'add'});
				this.load();
			},
			remove(id) {
				this.update({movieId : id, method : 'remove'});
				this.load();
			},
			increase(id) {
				this.update({movieId : id, method : 'increase'});
			},
			decrease(id) {
				this.update({movieId : id, method : 'decrease'});
			},
			clear() {
				this.update({method : 'clear'})
				this.load();
			},
			update(data) {
				axios.post('/project4/api/cart', data)
				.then((response) => {
					this.load();
				})
			},
			load() {
				axios.get('/project4/api/cart?detail=1')
				.then((response) => {
					this.cart = response.data;
					console.log(response.data);
				});
			}
		},
		computed: {
			totalItemCount: function () {
				count = 0;
				if (this.cart != null)
 					for (i = 0; i < this.cart.length; i++)
 						count += this.cart[i].quantity;
				return count;
			}
		},
		mounted() {
			this.load();
		}
	});