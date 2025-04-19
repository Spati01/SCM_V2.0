

console.log("contacts.js loaded");

const baseUrl = "http://localhost:8080";

// 1. send a GET request to /api/contacts
// 2. display contacts on the page

const viewContactModel = document.getElementById("view_contact_modal");


// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};

const modal = new Modal(viewContactModel, options, instanceOptions);

function openContactModal(){
    modal.show();
}

//Close the modal

function closeContactModal(){
    modal.hide();
}

// async function loadContactData(id){

//     //funcation call
//     console.log("Loading contact data for id: " + id);

   

//    try {
//     const data = await  (await  fetch(`http://localhost:8080/api/contacts/${id}`)).json();
//     console.log(data);
//     console.log(data.name);

//     document.getElementById("contact_name").innerHTML = data.name;
//     document.getElementById("contact_email").innerHTML = data.email;
//     openContactModal();

//    } catch (error) {
//      console.error("Error loading contact data: ", error);   


//    }
    

 
// }


async function loadContactData(id) {
    //function call to load data
    console.log(id);
    try {
        const data = await  (await  fetch(`${baseUrl}/api/contacts/${id}`)).json();
      console.log(data);
      document.querySelector("#contact_name").innerHTML = data.name;
      document.querySelector("#contact_email").innerHTML = data.email;
      document.querySelector("#contact_image").src = data.picture;
      document.querySelector("#contact_address").innerHTML = data.address;
      document.querySelector("#contact_phone").innerHTML = data.phoneNumber;
      document.querySelector("#contact_about").innerHTML = data.description;
      const contactFavorite = document.querySelector("#contact_favorite");
      if (data.favorite) {
        contactFavorite.innerHTML =
          "<i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i>";
      } else {
        contactFavorite.innerHTML = "Not Favorite Contact";
      }
  
      document.querySelector("#contact_website").href = data.websiteLink;
      document.querySelector("#contact_website").innerHTML = data.websiteLink;
      document.querySelector("#contact_linkedIn").href = data.linkedInLink;
      document.querySelector("#contact_linkedIn").innerHTML = data.linkedInLink;
      openContactModal();
    } catch (error) {
      console.log("Error: ", error);
    }
  }








  

  async function deleteContact(id) {
    // Detect if dark mode is active
    const isDarkMode = document.documentElement.classList.contains("dark");
  
    // Define colors based on theme
    const backgroundColor = isDarkMode ? "#1e1e2f" : "#ffffff"; // Dark or Light background
    const textColor = isDarkMode ? "#fff" : "#000"; // White or Black text
    const confirmButtonColor = isDarkMode ? "#ff4d4d" : "#d9534f"; // Red delete button
    const cancelButtonColor = isDarkMode ? "#3498db" : "#0275d8"; // Blue cancel button
  
    // Show confirmation dialog
    Swal.fire({
      title: "⚠️ Delete Contact?",
      text: "This action cannot be undone!",
      icon: "warning",
      iconHtml: "🗑️", // Custom Trash Icon
      background: backgroundColor,
      color: textColor,
      showCancelButton: true,
      confirmButtonColor: confirmButtonColor,
      cancelButtonColor: cancelButtonColor,
      confirmButtonText: "🗑️ Yes, Delete",
      cancelButtonText: "❌ Cancel",
      customClass: { popup: "swal-popup" },
    }).then((result) => {
      if (result.isConfirmed) {
        const url = `${baseUrl}/user/contacts/delete_contact/${id}`;
        console.log("Redirecting to:", url);
  
        // Show loading animation
        Swal.fire({
          title: "Deleting...",
          text: "Please wait while we remove the contact.",
          background: backgroundColor,
          color: textColor,
          allowOutsideClick: false,
          showConfirmButton: false,
          didOpen: () => Swal.showLoading(),
        });
  
        // Perform deletion via fetch
        fetch(url, { method: "GET" })
          .then((response) => {
            if (response.redirected) {
              window.location.href = response.url;
            } else {
              Swal.fire({
                title: "❌ Error",
                text: "Failed to delete contact.",
                icon: "error",
                background: backgroundColor,
                color: textColor,
              });
            }
          })
          .catch((error) => {
            console.error("Delete Error:", error);
            Swal.fire({
              title: "❌ Error",
              text: "An unexpected error occurred.",
              icon: "error",
              background: backgroundColor,
              color: textColor,
            });
          });
      } else if (result.dismiss === Swal.DismissReason.cancel) {
        // Show cancellation message
        Swal.fire({
          title: "❌ Cancelled",
          text: "Your contact is safe! 😊",
          icon: "info",
          iconHtml: "✅",
          background: backgroundColor,
          color: textColor,
          timer: 2000, // Auto-close after 2 seconds
          showConfirmButton: false,
          customClass: { popup: "swal-popup" },
        });
      }
    });
  }
  
  




  /*  count contact animation show */



