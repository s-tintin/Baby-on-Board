/**
 *  Functions for checkout page
 */

(function() {
	
	var frequencies = {1: 'Weekly', 2: 'Bi-Weekly', 3: 'Monthly'};
	var durations = {3: '3 Months', 6: '6 Months', 9: '9 Months', 12: '12 Months'};
	var product_headers = [{"name": "name", "display_name": "Product"},
						   {"name": "brand", "display_name": "Brand"},
						   {"name": "category", "display_name": "Category"},
						   {"name": "quantity", "display_name": "Quantity"},
						   {"name": "amount", "display_name": "Number"},
						   {"name": "price", "display_name": "Price ($)"}];
	var subscribed_items = [];
	var transaction_total = 0.0;
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
	 *  Toggling the dropdown 
	 */
	function onDropdownButtonClick() {
		var dropdowns = document.getElementsByClassName("dropdown-content");
		for(var i = 0; i < dropdowns.length; i++){
			var openDropdown = dropdowns[i];
			if (openDropdown.classList.contains('show')) {
				openDropdown.classList.remove('show');
			}
		}
		
	    this.parentElement.getElementsByClassName("dropdown-content")[0].classList.toggle("show");
	}

	/*
	 *  Close the dropdown menu if the user clicks outside of it
	 */
	function closeDropdownsOnOutsideClick(){
		
		window.onclick = function(event) {
			if (!event.target.matches('.dropbtn')) {
				var dropdowns = document.getElementsByClassName("dropdown-content");
				
				for(var i = 0; i < dropdowns.length; i++) {
					var openDropdown = dropdowns[i];
					if (openDropdown.classList.contains('show')) {
						openDropdown.classList.remove('show');
					}
				}
			}
		}
	}
	
	/*
	 *  Validate transcation details
	 */
	function validateTranscationDetails(event){
		var transaction = {};
		
		var transaction_type = event.target.getAttribute("data-type");
		transaction['payment_mode'] = transaction_type;
		transaction['date']  = new Date().toLocaleDateString();
		
		// Address validation
		var address = document.getElementById(transaction_type + "_addr").value;
		if(address.trim() == ""){
			showPopupMessage("error","Billing address is empty!");
			return false;
		}
		transaction['address'] = address.trim();
		
		// Name on card validation
		var name_on_card = document.getElementById(transaction_type + "_name");
		if(name_on_card != null){
			if(name_on_card.value.trim() == ""){
				showPopupMessage("error","Name on card is empty!");
				return false;
			}
			
			transaction['name_on_card'] = name_on_card.value.trim();
		}
		// Card number validation
		var card_no = document.getElementById(transaction_type + "_no");
		if(card_no != null){
			// If card number is empty
			if(card_no.value.trim() == ""){
				showPopupMessage("error","Card number is empty!");
				return false;
			}
			
			// If card number format is invalid
			var regex = /^([0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4})$/g;
			if(!regex.test(card_no.value)){
				showPopupMessage("error","Card number format is invalid");
				return false;
			}
			
			transaction['card_no'] = card_no.value.trim();
		}
		
		// Expiry date validation
		var exp_month = document.getElementById(transaction_type + "_exp_month");
		if(exp_month != null){
			// If expiry month is empty
			if(exp_month.value.trim() == ""){
				showPopupMessage("error","Expiry month is empty!");
				return false;
			}
			
			// If expiry month out of range
			if(parseInt(exp_month.value.trim()) < 1 || parseInt(exp_month.value.trim()) > 12){
				showPopupMessage("error","Expiry month is out of range!");
				return false;
			}
			
			transaction['exp_month'] = exp_month.value.trim();
		}
		
		var exp_year = document.getElementById(transaction_type + "_exp_year");
		if(exp_year != null){
			// If expiry year is empty
			if(exp_year.value.trim() == ""){
				showPopupMessage("error","Expiry year is empty!");
				return false;
			}
			
			// If expiry year out of range
			if(parseInt(exp_year.value.trim()) < 1900 || parseInt(exp_year.value.trim()) > 2100){
				showPopupMessage("error","Expiry year is out of range! Allowed range [1900-2100]");
				return false;
			}
			
			transaction['exp_year'] = exp_year.value.trim();
		}
		
		// CVV number validation
		var card_cvv = document.getElementById(transaction_type + "_cvv");
		if(card_cvv != null){
			// If CVV is empty
			if(card_cvv.value.trim() == ""){
				showPopupMessage("error","CVV Number is empty!");
				return false;
			}
			
			// If CVV has invalid format
			var regex = /^([0-9]{3})$/g;
			if(!regex.test(card_cvv.value)){
				showPopupMessage("error","CVV Number format is invalid!");
				return false;
			}
			
			transaction['card_cvv'] = card_cvv.value.trim();
		}
		
		transaction['amount'] = transaction_total;
		
		return transaction;
	}
	
	/*
	 *  Adds event listeners for payment submit button
	 */
	function addListenerForSubmitPayment(){
		var submit_elements = document.getElementsByClassName("submit-payment");
		for(var i = 0; i < submit_elements.length; i++){
			submit_elements[i].addEventListener("click", function(e){
				
				var transaction = validateTranscationDetails(e);
				
				if(!transaction){
					return false;
				}
				
				transaction['subscribed_items'] = subscribed_items;

				var xhr = new XMLHttpRequest();
				
				xhr.onreadystatechange = function() {
				    if(xhr.readyState == 4 && xhr.status == 200) {
				    	
				    	var response = JSON.parse(xhr.responseText);
				    	var result = "fail";
				    	if(response['status'] > 0){
				    		result = "pass";
				    		if(checkoutType == "cart"){
				    			modifyCartItem({'cust_id': user.id}, "delete", result);
				    		}
				    		else{
				    			navigateToConfirmation(result);
				    		}
				    	}
				    	else{
				    		navigateToConfirmation(result);
				    	}
				    }
				}
				
				xhr.open("POST", "CustomerSubscriptions", true);
				xhr.setRequestHeader('Content-Type', 'application/json');
				xhr.send(JSON.stringify(transaction));
			});
		}
	}
	
	/*
	 *  Navigate to confirmation
	 */
	function navigateToConfirmation(result){
		var confirmation_url =  window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + contextPath + "/confirmation?result=" + result;
		window.location.href = confirmation_url;
	}
	
	/*
	 *  Initializing horizontal tabs
	 */
	function initHorizontalTabs(){
		$('#horizontalTab').easyResponsiveTabs({
			type: 'default', //Types: default, vertical, accordion           
			width: 'auto', //auto or any width like 600px
			fit: true   // 100% fit in a container
		});
		
		addListenerForSubmitPayment();
	}
	
	/*
	 *  Adds event listeners for dropdown contents
	 */
	function addEventListenersForDropdownItems(){
		var items = document.getElementsByClassName("dropdown-item");
		
		for(var j = 0; j < items.length; j++){
			var selected_item = items[j];
			selected_item.addEventListener("click",function(e){
				var parent = this.parentElement;
				parent.setAttribute("data-selected", this.getAttribute("data-val"));
				parent.parentElement.getElementsByClassName("dropbtn")[0].innerHTML = parent.getAttribute("data-name") + ": " + this.innerHTML;
				
				var sub_index = parent.parentElement.parentElement.parentElement.getAttribute("data-id");
				
				if(parent.getAttribute("data-name").toLowerCase() == "duration"){
					subscribed_items[sub_index]["duration"] = parseInt(this.getAttribute("data-val"));
				}
				else if(parent.getAttribute("data-name").toLowerCase() == "frequency"){
					subscribed_items[sub_index]["frequency"] = frequencies[this.getAttribute("data-val")].toLowerCase();
				}
			});
		}
	}
	
	/*
	 *  On subscription quantity change
	 */
	function onQuantityChange(e){
		var value = this.value;
		var sub_index = this.parentElement.parentElement.parentElement.getAttribute("data-id");
		subscribed_items[sub_index]['quantity'] = parseInt(value);
		
		var sub_total_element = this.parentElement.parentElement.parentElement.getElementsByClassName("sub-total")[0];
		var new_total = subscribed_items[sub_index]['quantity'] * subscribed_items[sub_index]['total'];
		var content = "Sub total: $" + Number(new_total).toFixed(2);
		sub_total_element.textContent = content;
		
		var temp_total = 0.0;
		for(i = 0; i < subscribed_items.length; i++){
			temp_total += subscribed_items[i]['quantity'] * subscribed_items[i]['total'];
		}
		
		transaction_total = temp_total;
		
		document.getElementById("main-total").textContent = 'Total Amount: $' + Number(transaction_total).toFixed(2);
	}
	
	/*
	 *  Fetch subscription details success handler
	 */
	function frameOrderDetailsSuccessHandler(response, subscription_items){
		response = JSON.parse(response);
		
		for(var n = 0; n < response.length; n++){
			
			var order_details_string = '<div class="subscription-item" data-id="' + n + '">';
			
			order_details_string += '<div class="subscription-details">' +
									'<div class="wi-25-di-in-bk fo-we-bo">' + (n+1) + '. ' + response[n]['name'] + '</div>' +
									'<div class="age-group wi-25-di-in-bk fo-si-13">Age Group: ' + response[n]['ageGroup']['name'] + ' (' + response[n]['ageGroup']['description'] + ')</div>' +
									'<div class="sub-quantity wi-10-di-in-bk fo-si-13">' +
									'<span class="di-in-bl">Quantity: </span>' +
									'<input class="di-in-bl ou-no input-value quantity" type="number" value="' + (subscription_items[n].quantity ? subscription_items[n].quantity : 1)  + '" name="quantity" min="1" max="10">' +
									'</div>' +
									'<div id="duration-dropdown" class="dropdown wi-15-di-in-bk fo-we-bo">' +
									'<button class="dropbtn">Duration: ' + (subscription_items[n].duration ? durations[subscription_items[n].duration]: durations['12']) + '</button>' +
									'<div id="duration-dropdown-content" class="dropdown-content" data-name="Duration" data-selected="' + (subscription_items[n].duration ? subscription_items[n].duration: 12) + '">';
			
			// Framing Duration dropdown
			for(var j in durations){
				order_details_string += '<a class="dropdown-item" href="#" data-val="' + j + '">' + durations[j] + '</a>';
			}
			
			order_details_string += '</div>' +
									'</div>' +
									'<div id="frequency-dropdown" class="dropdown wi-15-di-in-bk fo-we-bo">' +
									'<button class="dropbtn">Frequency: ' + frequencies['1'] + '</button>' +
									'<div id="frequency-dropdown-content" class="dropdown-content" data-name="Frequency" data-selected="' + 1 + '">';
			
			// Framing Frequency drop down
			for(var k in frequencies){
				order_details_string += '<a class="dropdown-item" href="#" data-val="' + k + '">' + frequencies[k] + '</a>';
			}
			
			order_details_string += '</div>' +
									'</div>' +
									'</div>';
			
			order_details_string += '<div class="main">' +
									'<div>' + 
									'<table width="200" border="1">' +
									'<tbody>' +
									'<tr>';
			
			// Framing products table
			if(response[n].products && response[n].products.length){
				
				order_details_string += '<th scope="col">S.No</th>';
				
				for(var i = 0; i < product_headers.length; i++){
					var header = product_headers[i];
					order_details_string += '<th scope="col text-capitalize">' + header.display_name + '</th>';
				}
				
				order_details_string += '<th scope="col">Total ($)</th>';
				order_details_string += '</tr>';
				
				var total = 0.0;
				var number = 0, price = 0;
				
				for(var i = 0; i < response[n].products.length; i++){
					var product_selected = response[n].products[i];
					
					order_details_string += '<tr>' + 
											'<td>' + (i+1) + '</td>';
					
					for(var j = 0; j < product_headers.length; j++){
						var header = product_headers[j];
						if(header.name == "amount"){
							number = product_selected[header.name];
							order_details_string += '<td>' + product_selected[header.name] + '</td>';
						}
						else if(header.name == "price"){
							price = product_selected[header.name];
							order_details_string += '<td>' + Number(product_selected[header.name]).toFixed(2) + '</td>';
						}
						else{
							order_details_string += '<td>' + product_selected[header.name] + '</td>';
						}
					}
					
					total += number * price;
					
					order_details_string += '<td>' + Number(number * price).toFixed(2) + '</td>';
					order_details_string += '</tr>';
				}
				
				order_details_string += '</tbody></table></div></div>';
				order_details_string += '<div class="sub-total">Sub total: $' + Number((subscription_items[n].quantity ? subscription_items[n].quantity : 1) * total).toFixed(2) + '</div>';
				
				transaction_total += total;
			}
			
			subscribed_items.push({
				customer_id: user.id,
				subscription_id: response[n]['id'],
				frequency: frequencies['1'].toLowerCase(),
				duration: duration ? parseInt(duration) : 12,
				quantity: (subscription_items[n].quantity ? subscription_items[n].quantity : 1),
				start_date: new Date().toLocaleDateString(),
				total: total
			});
			
			order_details_string += '</div>';
			
			document.getElementById("cart-container").innerHTML += order_details_string;
		}
		
		document.getElementById("cart-container").innerHTML += '<div id="main-total">Total Amount: $' + Number(transaction_total).toFixed(2) + '</div>';
		
		// Adding event listeners for dropdowns
		var dropdown_buttons = document.getElementsByClassName("dropbtn");
		for(var l = 0; l < dropdown_buttons.length; l++){
			dropdown_buttons[l].addEventListener("click", onDropdownButtonClick);
		}
		addEventListenersForDropdownItems();
		
		// Adding event listeners for quantity
		var quantity_buttons = document.getElementsByClassName("quantity");
		for(var l = 0; l < quantity_buttons.length; l++){
			quantity_buttons[l].addEventListener("click", onQuantityChange);
		}
		
	}
	
	/*
	 * Fetches subscription details from database
	 */
	function frameOrderDetails(){
		
		var subscription_items = [];
		var sub_ids = [];
		
		if(checkoutType == "cart"){
			
			var interval = setInterval(function(){
				if(cartItems == null){return;}
				clearInterval(interval);
				
				for(var j = 0; j < cartItems.length; j++){
					subscription_items.push({
						quantity: cartItems[j].quantity,
						price: cartItems[j].price,
						age_group_id: cartItems[j].ageGroupId,
						subscription_id: cartItems[j].subscriptionId
					});
					sub_ids.push(cartItems[j].subscriptionId);
				}
				
				getSubscriptionDetails(sub_ids, subscription_items);
				
			}, 10);
		}
		else{
			subscription_items.push({
				age_group_id: ageGroupId,
				sub_id: subscriptionId,
				duration: duration
			});
			sub_ids.push(subscriptionId);
			getSubscriptionDetails(sub_ids, subscription_items);
		}
	}
	
	/*
	 *  Fetches subscription details
	 */
	function getSubscriptionDetails(sub_ids, subscription_items){
		
		if(sub_ids.length == 0){
			return;
		}
		
		var xhttp = new XMLHttpRequest();
	    xhttp.onreadystatechange = function() {
	        if (this.readyState == 4 && this.status == 200) {
	        	frameOrderDetailsSuccessHandler(this.responseText, subscription_items);
	        }
	    };
	    
	    xhttp.open("GET", "GetSubscriptionInfo?subscription=" + sub_ids.join(','), true);
	    xhttp.send();
	}
	
	/*
	 *  Add cart item to database
	 */
	function modifyCartItem(request_object, type, result){
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
		    	renderCartDisplaySuccessHandler(xhr.responseText, result);
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
	function renderCartDisplaySuccessHandler(response, result){
		
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
		
		if(result){
			navigateToConfirmation(result);
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
	 *  Initializes event listeners for checkout page
	 */
	function initEventListeners(){
		initializeCartEvents();
		document.getElementsByClassName("cartCheckout_btn")[0].addEventListener("click", proceedToCartCheckout);
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
	 *  Initializes checkout page
	 */
	function initCheckout(){
		//Setting logout path
		var logoutPath = contextPath + "/logout";
		var indexPath = contextPath + "/index";
		var transactionPath=contextPath + "/transactionHistory";
		var userPath=contextPath + "/userProfile";
		
	    document.getElementById("logout_button").setAttribute("href", logoutPath);
	    document.getElementById("transaction").setAttribute("href", transactionPath);
	    document.getElementById("logo-link").setAttribute("href", indexPath);
	    document.getElementById("user_profile").setAttribute("href", userPath);
	    
	    renderCartDisplay();
	    frameOrderDetails();
		initHorizontalTabs();
		closeDropdownsOnOutsideClick();
		
		initEventListeners();
	}
	
	/*
	 *  Call initialize function on document ready
	 */
	$(document).ready(initCheckout);
	
})();