// Hàm chuyển đổi nội dung giữa các tab (Note / Friend / Info)
function switchSection(section, element) {
    if (element) {
        var menuItems = document.querySelectorAll('.menu-item');
        menuItems.forEach(function(item) {
            item.classList.remove('active');
        });
        element.classList.add('active');
    }

    var contentDiv = document.getElementById('dynamic-content');
    var xhr = new XMLHttpRequest();
    
    // Thêm tham số _id để xem được tường của người khác
    var url = contextPath + "/dashboard-note?action=load-section&section=" + section + "&_id=" + serverOwnerId; // Updated

    xhr.open('GET', url, true);
    
    xhr.onload = function() {
        if (this.status === 200) {
            contentDiv.innerHTML = this.responseText;
        }
    };
    
    xhr.send();
}

// Hàm xử lý tìm kiếm (Debounce)
var searchTimeout; // New
function handleSearch() { // New function
    var query = document.getElementById('searchInput').value; // New
    var contentDiv = document.getElementById('dynamic-content'); // New

    clearTimeout(searchTimeout); // New

    if (!query || query.trim() === "") { // New
        switchSection('wall'); // New
        return; // New
    }

    searchTimeout = setTimeout(function() { // New
        var xhr = new XMLHttpRequest(); // New
        var url = contextPath + "/dashboard-note?action=search-user&search=" + encodeURIComponent(query); // New
        
        xhr.open('GET', url, true); // New
        
        xhr.onload = function() { // New
            if (this.status === 200) { // New
                contentDiv.innerHTML = this.responseText; // New
            } // New
        }; // New
        xhr.send(); // New
    }, 500); // New
}

// Hàm ẩn/hiện danh sách bạn bè cho Modal Tạo mới
function toggleFriendList() {
    var select = document.getElementById("accessLevelSelect");
    var section = document.getElementById("friendSelectSection");
    if(select.value === "PROTECTED_1") {
        section.classList.remove("hidden");
        section.style.display = "block"; // Updated
    } else {
        section.classList.add("hidden");
        section.style.display = "none"; // Updated
    }
}

// Hàm ẩn/hiện danh sách bạn bè cho Modal Chỉnh sửa
function toggleEditFriendList() { // New function
    var select = document.getElementById("edit_accessLevelId"); // New
    var section = document.getElementById("edit_friendSelectSection"); // New
    if(select.value === "PROTECTED_1") { // New
        section.classList.remove("hidden"); // New
        section.style.display = "block"; // New
    } else { // New
        section.classList.add("hidden"); // New
        section.style.display = "none"; // New
    } // New
}

// Hàm đóng mở Modal
function toggleModal(modalId, show) {
    var modal = document.getElementById(modalId);
    if(show) {
        modal.style.display = 'flex';
    } else {
        modal.style.display = 'none';
    }
}

// Hàm hỗ trợ điền dữ liệu vào Modal Sửa
function openEditModal(id, title, content, accessLevel, allowComment, topicId) { // New function
    document.getElementById('edit_id').value = id; // New
    document.getElementById('edit_title').value = title; // New
    document.getElementById('edit_content').value = content; // New
    document.getElementById('edit_accessLevelId').value = accessLevel; // New
    document.getElementById('edit_allowComment').value = allowComment; // New
    
    if(document.getElementById('edit_topicId')) { // New
        document.getElementById('edit_topicId').value = topicId || "general"; // New
    } // New

    toggleEditFriendList(); // New
    toggleModal('editNoteModal', true); // New
}

// Đóng modal khi click ra ngoài
window.onclick = function(event) {
    if (event.target.classList.contains('modal-overlay')) {
        event.target.parentElement.style.display = "none";
    }
}