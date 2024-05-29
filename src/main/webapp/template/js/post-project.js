$(document).ready(function () {


});

// let allFiles = [];
// let form = document.getElementsByClassName("form-img");
// let input = document.getElementById("file_input");
// let container = document.getElementsByClassName("img-container");
// if (input.files.length != 0) {
//     container[0].parentElement.classList.add('d-block')
//     container[0].parentElement.classList.remove('d-none')
// } else {
//     container[0].parentElement.classList.add('d-none')
//     container[0].parentElement.classList.remove('d-block')
// }
// input.addEventListener('change', function () {
//     let files = this.files;
//     for (let i = 0; i < files.length; i++) {
//         allFiles.push(files[i])
//     }
//     showImage();
// })
// const showImage = () => {
//     if (input.files.length != 0) {
//         container[0].parentElement.classList.add('d-block')
//         container[0].parentElement.classList.remove('d-none')
//     } else {
//         container[0].parentElement.classList.add('d-none')
//         container[0].parentElement.classList.remove('d-block')
//     }
//     let images = ' ';
//     allFiles.forEach((e, i) => {
//         images += '<div class="image position-relative border-radius"><img src="' + URL.createObjectURL(e) + '" alt="" class="border"> ' +
//             '<div class="position-absolute " > <i class="fa-solid fa-xmark" onclick="delImage(' + i + ')" style=""></i></div></div>'
//     })
//     container[0].innerHTML = images
// }
// let dt = new DataTransfer();
// const delImage = index => {
//     let dt = new DataTransfer();
//     for (let i = 0; i < input.files.length; i++) {
//         if (index !== i)
//             dt.items.add(input.files[i]) // here you exclude the file. thus removing it.
//     }
//     input.files = dt.files
//     allFiles = Array.from(input.files)
//     showImage()
// }


$(document).ready(function () {
    $.ajax({
        url: '/api/advise',
        type: 'get',
        success: function (data) {
            data = JSON.parse(data);
            data.provinces.forEach((e, i) => {
                $('#address').append('<option value="' + e.id + '">' + e.name + '</option>')
            })
            data.categories.forEach((e, i) => {
                $('#category').append('<option value="' + e.id + '">' + e.name + '</option>')
            })
            data.services.forEach((e, i) => {
                $('#services').append('<option value="' + e.id + '">' + e.name + '</option>')
            })
            data.projects.forEach((e, i) => {
                $('#itProject').append('<option value="' + e + '">' + e + '</option>')
            })
            $('.mdb-select').materialSelect();
        },
        error: function (e) {
            console.log(e)
        }

    })

//     $('select.services').change(function () {
//         let value = $(this).val().toString();
//         console.log(value)
//         $.ajax({
//             url: '/session/cart',
//             type: 'get',
//             data: {
//                 name: 'services',
//                 value: value
//             },
//             success: function (data) {
//                 console.log(data)
//             },
//             error: function (e) {
//                 console.log(e)
//             }
//         })
//     })
// })
// $('.form-input').blur(function () {
//     let name = $(this).attr("name");
//     let value = $(this).val();
//     $.ajax({
//         url: '/session/cart',
//         type: 'get',
//         data: {
//             name: name,
//             value: value
//         },
//         success: function (data) {
//         },
//         error: function (e) {
//         }
//     })

});


function like(project, id) {
    console.log(project)
    $.ajax({
        url: "/api/save_project" + id,
        type: "GET",
        success: function (response) {
            console.log(response);
            let resp = JSON.parse(response);
            if (resp.name == 'save') {
                project.classList.replace("fa-regular", "fa-solid")
            } else if (resp.name == 'delete')
                project.classList.replace("fa-solid", "fa-regular")
        },
        error: function (response) {
            let resp = JSON.parse(response.responseText);
            window.location.href = resp.data;
        }
    })
}


function fetchErr(name, mess) {
    switch (name) {
        case'email':
            let email = document.getElementById('form-email')
            email.classList.add('border-danger');
            email.classList.add('text-danger');
            email.value = "";
            email.setAttribute('value', "");
            break;
        case'address':
            let address = document.getElementById('address')
            address.classList.add('border-danger');
            address.classList.add('text-danger');
            address.value = "";
            address.setAttribute('value', "");
            address.setAttribute('placeholder', mess);
            break;
        case'category':
            let category = document.getElementById('category')
            category.classList.add('border-danger');
            category.classList.add('text-danger');
            category.value = "";
            category.setAttribute('value', "");
            category.setAttribute('placeholder', mess);
            break;
        case'width':
            let width = document.getElementById('area-width')
            width.classList.add('border-danger');
            width.classList.add('text-danger');
            width.value = "";
            width.setAttribute('value', "");
            width.setAttribute('placeholder', mess);
            break;
        case'height':
            let height = document.getElementById('area-length')
            height.classList.add('border-danger');
            height.classList.add('text-danger');
            height.value = "";
            height.setAttribute('value', "");
            height.setAttribute('placeholder', mess);
            break;
        case'services':
            let services = document.getElementById('services')
            services.classList.add('border-danger');
            services.classList.add('text-danger');
            services.value = "";
            services.setAttribute('value', "");
            services.setAttribute('placeholder', mess);
            break;
        case'itProject':
            let itProject = document.getElementById('itProject')
            itProject.classList.add('border-danger');
            itProject.classList.add('text-danger');
            itProject.value = "";
            itProject.setAttribute('value', "");
            itProject.setAttribute('placeholder', mess);
            break;
    }
}


let email = document.getElementById('form-email')
email.addEventListener('click', function () {
    email.classList.remove('border-danger');
    email.classList.remove('text-danger');
    email.setAttribute('placeholder', "");
})

let address = document.getElementById('address')
address.addEventListener('click', function () {
    address.classList.remove('border-danger');
    address.classList.remove('text-danger');
    address.setAttribute('placeholder', "");
})

let category = document.getElementById('category')
category.addEventListener('click', function () {
    category.classList.remove('border-danger');
    category.classList.remove('text-danger');
    category.setAttribute('placeholder', "");
})

let width = document.getElementById('area-width')
width.addEventListener('click', function () {
    width.classList.remove('border-danger');
    width.classList.remove('text-danger');
    width.setAttribute('placeholder', "");
})

let height = document.getElementById('area-length')
height.addEventListener('click', function () {
    height.classList.remove('border-danger');
    height.classList.remove('text-danger');
    height.setAttribute('placeholder', "");
})

let services = document.getElementById('services')
services.addEventListener('click', function () {
    services.classList.remove('border-danger');
    services.classList.remove('text-danger');
    services.setAttribute('placeholder', "");
})

let itProject = document.getElementById('itProject')
itProject.addEventListener('click', function () {
    itProject.classList.remove('border-danger');
    itProject.classList.remove('text-danger');
    itProject.setAttribute('placeholder', "");
})
