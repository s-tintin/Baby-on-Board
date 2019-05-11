/**
 *  Functions for user profile page
 */

(function() {
	
	var cartItems = null;
	
	/*
	 *  Popup message rendering 
	 */
	function showPopupMessage(type, message){
		var messageElement = document.getElementById("pop-up-message");
		messageElement.classList.add(type);
		messageElement.innerHTML = message;
		messageElement.classList.add("visible");
		
		setTimeout(function(){
			messageElement.classList.remove("visible");
			messageElement.classList.remove(type);
		}, 4000);
	}
	
	/*
	 *  Add cart item to database
	 */
	function modifyCartItem(request_object, type){
		document.getElementById("overlay").classList.remove("hide");
		
		var url = "";
		
		if(type == "add"){
			url = "AddCartItem";
		}
		else if(type == "update"){
			url = "UpdateCartItem";
		}
		else if(type == "delete"){
			url = "DeleteCartItem";
		}
		
		var xhr = new XMLHttpRequest();
		
		xhr.onreadystatechange = function() {
		    if(xhr.readyState == 4 && xhr.status == 200) {
		    	renderCartDisplaySuccessHandler(xhr.responseText);
		    	document.getElementById("overlay").classList.add("hide");
		    }
		}
		
		xhr.open("POST", url, true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send(JSON.stringify(request_object));
	}
	
	/*
	 *  Get cart items success handler
	 */
	function renderCartDisplaySuccessHandler(response){
		if(response == null){
			return;
		}
		
		cartItems = JSON.parse(response);
		
		if(response.length == 0){
			return;
		}
		
		var cart_string = "";
		
		for(var p = 0; p < cartItems.length; p++){
			cart_string += '<tr><td>' + (p+1) + '</td>'
						+ '<td>' + cartItems[p].ageGroupName + '</td>'
						+ '<td>' + cartItems[p].subscriptionName + '</td>'
						+ '<td>' + cartItems[p].quantity + '</td>'
						+ '<td>' + Number(cartItems[p].price).toFixed(2) + '</td>'
						+ '<td><button class="delete_cart" data-sub-id="' + cartItems[p].subscriptionId + '">Delete</button></td></tr>';
		}
		cart_string += '</tr>';
		
		document.getElementById("cart-table1").innerHTML = cart_string;
		
		// Update cart item count
		document.getElementById("cart-count").innerHTML = cartItems.length;
		
		// Delete from cart event listeners
		var cart_delete_buttons = document.getElementById("cart-table1").getElementsByClassName("delete_cart");
		
		for(var l = 0; l < cart_delete_buttons.length; l++){
			cart_delete_buttons[l].addEventListener("click", function(e){
				
				var sub_id = e.target.getAttribute("data-sub-id");
				var request_object = {'cust_id': user.id, 'sub_id': sub_id};
				modifyCartItem(request_object, "delete");
			});
		}
	}
	
	/*
	 *  Update cart model in UI
	 */
	function renderCartDisplay(){
		
		var xhttp = new XMLHttpRequest();
	    xhttp.onreadystatechange = function() {
	        if (this.readyState == 4 && this.status == 200) {
	        	renderCartDisplaySuccessHandler(this.responseText);
	        }
	    };
	    
	    xhttp.open("GET", "FetchCartItems?id=" + user.id, true);
	    xhttp.send();
	}
	
	/*
	 * 	Add event handlers to cart
	 */
	function initializeCartEvents(){
		var modal = document.getElementById('cart-modal');

		// Get the button that opens the modal
		var btn = document.getElementById("cart_btn");

		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("close")[0];
		
		// When the user clicks the button, open the modal 
		btn.addEventListener("click", function(){			
			modal.style.display = "block";
		});
		   
		// When the user clicks on <span> (x), close the modal
		span.addEventListener("click", function(){	
			modal.style.display = "none";
		});
		
		// When the user clicks anywhere outside of the modal, close it
		window.addEventListener("click", function(event){
		    if (event.target == modal) {
		        modal.style.display = "none";
		    }
		});
		
	}
	
	/*
	 *  Validate customer details
	 */
	function customerValidate(e){
		var name = document.forms["editcustomer"]["name"].value;
		var email = document.forms["editcustomer"]["email"].value;
		var phone = document.forms["editcustomer"]["phone"].value;
		var password = document.forms["editcustomer"]["password"].value;
		var rpassword = document.forms["editcustomer"]["retry-password"].value;
		
		var emailLegalReg =  /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
		var phoneReg = /^[(]{0,1}[0-9]{3}[)]{0,1}[-\s\.]{0,1}[0-9]{3}[-\s\.]{0,1}[0-9]{4}$/;
		
		if (password == "") {
	        e.preventDefault();
	        e.stopPropagation();
	        showPopupMessage("error", "Password must be filled out");
	        document.forms["editcustomer"]["password"].focus();
	    }else if (rpassword == "") {
	        e.preventDefault();
	        e.stopPropagation();
	        showPopupMessage("error", "Retype Password must be filled out");
	        document.forms["editcustomer"]["retry-password"].focus();
	    }else if (name == "") {
	        e.preventDefault();
	        e.stopPropagation();
	        showPopupMessage("error", "Full Name must be filled out");
	        document.forms["editcustomer"]["name"].focus();
	    }else if (email == "") {
	        e.preventDefault();
	        e.stopPropagation();
	        showPopupMessage("error", "Email must be filled out");
	        document.forms["editcustomer"]["email"].focus();
	    }else if(!emailLegalReg.test(email)){
	    	e.preventDefault();
	        e.stopPropagation();
	        showPopupMessage("error", "Please enter a valid email id");
	        document.forms["editcustomer"]["email"].focus();
	    }else if(phone == "") {
	        e.preventDefault();
	        e.stopPropagation();
	        showPopupMessage("error", "Phone must be filled out");
	        document.forms["editcustomer"]["phone"].focus();
	    }else if(!phoneReg.test(phone)) {
	        e.preventDefault();
	        e.stopPropagation();
	        showPopupMessage("error", "Phone number is invalid");
	        document.forms["editcustomer"]["phone"].focus();
	    }else if(password != rpassword){
	    	e.preventDefault();
	        e.stopPropagation();
	    	showPopupMessage("error", "Passwords do not match");
	        document.forms["editcustomer"]["password"].focus();
	    }
	}
	
	/*
	 *  Fills the form with customer details
	 */
	function loadCustomerDetails(){
		
		document.getElementById("user-heading-name").textContent = "Welcome, " + user.fullname;
		
		document.forms["editcustomer"]["id"].value = user.id;
		document.forms["editcustomer"]["username"].value = user.username;
		document.forms["editcustomer"]["name"].value = user.fullname;
		document.forms["editcustomer"]["email"].value = user.email;
		document.forms["editcustomer"]["phone"].value = user.phone;
		document.forms["editcustomer"]["password"].value = user.password;
		document.forms["editcustomer"]["retry-password"].value = user.password;
		
		document.getElementById("edit-customer").addEventListener("submit", function(e){ customerValidate(e);});
	}
	
	/*
	 *  Proceed to Cart checkout
	 */
	function proceedToCartCheckout(){
		if(cartItems.length == 0){
			showPopupMessage("error", "Add items to cart before checkout!");
			return;
		}
		
		var checkout_url =  window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + contextPath + "/checkout?type=cart";
		window.location.href = checkout_url;
	}
	
	/*
	 *  Initializes user profile page
	 */
	function init(){
		//Setting logout and logo path
		var logoutPath = contextPath + "/logout";
		var indexPath = contextPath + "/index";
		var trasactionPath=contextPath +"/transactionHistory";
		var userPath=contextPath + "/userProfile";
		
	    document.getElementById("logout_button").setAttribute("href", logoutPath);
	    document.getElementById("user_profile").setAttribute("href", userPath);
	    document.getElementById("transaction").setAttribute("href", trasactionPath);
	    document.getElementById("logo-link").setAttribute("href", indexPath);
	    
	    loadCustomerDetails();
	    
	    renderCartDisplay();
	    initializeCartEvents();
	    document.getElementsByClassName("cartCheckout_btn")[0].addEventListener("click", proceedToCartCheckout);
	}
	
	/*
	 *  Call initialize function on script execution
	 */
	init();
})();