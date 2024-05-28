let swiper = new Swiper('#catch', {
    slidesPerView: 1,
    // spaceBetween: 30,
    pagination: {
        el: ".swiper-pagination",
        clickable: true,

    },
    breakpoints: {
        675: {
            slidesPerView: 2
        },
        800: {
            slidesPerView: 3
        }
    }
});
let items = document.querySelectorAll('.item-selector');
let cur = document.getElementsByClassName('item-selector active')
for (const item of items) {
    item.addEventListener('click', function () {
        for (const i of items) {
            if (i != item) {
                i.classList.remove('active')
                document.getElementById(i.getAttribute('type')).classList.remove('active')
            }
        }
        item.classList.add('active')
        document.getElementById(item.getAttribute('type')).classList.add('active')
    })
}

// while click fa-regular fa-bookmark change to fa-solid fa-bookmark


getServices("home", "services-container");
$(document).ready(function () {
    $('.fa-bookmark').click(function () {
        $(this).toggleClass('fa-regular fa-bookmark fa-solid fa-bookmark');
    });
    $.ajax({
        url: '/api/home/slides',
        type: 'Get',
        success: function (data) {
            let slides = JSON.parse(data).data;
            let slide = document.getElementById('slide-container');
            slide.innerHTML = "";
            slides.forEach((x, index) => {

                let active = index === 0 ? "active" : "";
                slide.innerHTML += `<div class="carousel-item ${active} w-100">
                <img class="d-block w-100 " src="${x.avatar}"
                     alt="${x.title}">`
            })
        },
    })
//lấy danh mục dự án
    $.ajax({
        url: "/api/home/categories",
        type: 'Get',
        success: function (data) {
            let categories = JSON.parse(data).data;
            let categoryContainer = document.getElementById('category-container');
            categoryContainer.innerHTML = "";
            console.log(categories);
            //ClickCategory
            categories.forEach((x, index) => {
                let active = index === 0 ? "active" : "";
                categoryContainer.innerHTML +=
                    ` <li class="category-item">
                         <button class="item-selector"
                                   role="tab"
                                    onclick="ClickCategory(${x.id})"
                                   title="${x.name}">
                              XÂY DỰNG ${x.name}
                          </button>
               </li>`
            })
            // láy dự án đầu tiên khi load vào trang
            ClickCategory(categories[0].id)
        },
    })
});

function saveContact() {
    $.ajax({
        url: '/api/contact/save',
        type: 'Post',
        dataType: 'json',
        data: {
            fullName: $('#fullName').val(),
            email: $('#email').val(),
            address: $('#address').val(),
            phone: $('#phone').val(),
            content: $('#content').val(),
        },
        success: function (data) {
            delayNotify(1000, data.message);
            if (data.name == 'success') {
                setTimeout(() => {
                    window.location.reload();
                }, 1000);
            }
        },
        error: function (data) {
            //bắt lỗi email
            console.log(data.message);
            delayNotify(10000, "Vui lòng nhập đúng định dạng");
            if (data.name == 'error') {
                setTimeout(() => {
                    window.location.reload();
                }, 1000);
            }
        }

    })
}


// 0  người dùng ấn nút loại dự án có trên màn hình
// 1.1 ClickCategory
function ClickCategory(id) {
    // 1.1.1 thực hiện tạo đường dẫn đich với thông tin id của lại dự án được chọn
    endPoint = "/api/home/projects/" + id
    //1.1.1 thực hiện tạo đường dẫn đich với thông tin id của lại dự án được chọn
    $.ajax({
        url: endPoint,
        type: 'Get',
        success: function (data) {
            resdata = JSON.parse(data).data
            console.log(resdata)
            let list = resdata
            // 9 displayProjects
            displayProjects(list)
        },
        error: function (data) {

        }
    })
}

function displayProjects(list) {
    let containter = document.getElementById('project-container');
    let project = "";
    containter.innerHTML = "";
    console.log("list")
    console.log(list)
    for (const x of list) {
        project += drawProject(x);
        console.log(project)
    }
    containter.innerHTML = project;
}

function drawProject(x) {
    let project = "";
    if (x.isSave) project += ` <i class="fa-solid fa-bookmark position-absolute" onclick="like(this)" style="z-index: 1000"></i>`
    else project += `<i class="fa-regular fa-bookmark position-absolute" onclick="like(this)" style="z-index: 1000"></i>`;
    return `<div` +
        ` class="col-lg-3 col-md-4 col-sm-6 mb-4 overflow-hidden position-relative projectCard-container">`
        + `<div`
        + ` class="bg-image hover-image hover-zoom ripple shadow-1-strong rounded-5 w-100 d-block">` + project + `<a href="/post/project/` + x.id + `">`
        + `<img src="` + x.avatar + `"`
        + ` class="w-100">`
        + ` <input type="hidden" class="project-id" value=` + x.id + `>`
        + ` <div class="w-100 position-absolute projectCard-content">`
        + `  <div class="mask justify-content-center d-flex h-100"`
        + ` style="background-color: rgba(48, 48, 48, 0.72);">`
        + `<div class="align-items-center flex-column d-flex w-100"><h6`
        + ` class="text-white text-center pl-2 pr-2 projectTitle-center text-uppercase">`
        + x.title + `</h6>`
        + `<p class="text-white p-0 id-project">`
        + `<strong>MDA:` + x.id + `</strong>`
        + `</p>`
        + `<p class="text-white p-4 vanBan">` + x.description + `</p>`
        + `</div>`
        + `</div></div></a></div></div>`
}

function like(project) {
    let id = $(project).parent().find('.project-id').val();
    console.log(id);
    $.ajax({
        url: "/api/save_project",
        type: "GET",
        data: {
            "projectId": id
        },
        success: function (response) {
            console.log(response);
            let resp = JSON.parse(response);
            if (resp.name == 'save') {
                project.classList.replace("fa-regular", "fa-solid")
            } else if (resp.name == 'delete')
                project.classList.replace("fa-solid", "fa-regular")
            //= "fa-solid fa-bookmark position-absolute";
            // console.log(p);
        },
        error: function (response) {
            console.log(response.responseText)
            let resp = JSON.parse(response.responseText);
            window.location.href = resp.data;
        }
    })
}


function fetchErr(name, mess) {
    console.log(name, mess)
    switch (name) {
        case 'email':
            let email = document.getElementById('email');
            email.classList.add('border-danger');
            email.classList.add('text-danger');
            email.value = "";
            email.setAttribute('value', "");
            // email.setAttribute('placeholder', mess);
            break;
    }
}


let fullName = document.getElementById('email');
fullName.addEventListener('click', function () {
    fullName.classList.remove('border-danger');
    fullName.classList.remove('text-danger');
    fullName.setAttribute('placeholder', "Email");
})
