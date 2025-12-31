/*variable*/
let searchTimeout = null;
/*function*/
function toggleModal(modalId, show) {
    const modal = document.getElementById(modalId);
    if (!modal) return;
    if (show) {
        modal.style.display = "block";
        modal.classList.add("show");
        document.body.style.overflow = "hidden";
    } else {
        modal.style.display = "none";
        modal.classList.remove("show");
        document.body.style.overflow = "auto";
    }
}

function toggleFriendList() {
    const selectBox = document.getElementById("accessLevelSelect");
    const friendSection = document.getElementById("friendSelectSection");
    if (!selectBox || !friendSection) return;

    if (selectBox.value === "PROTECTED_1") {
        friendSection.classList.remove("hidden");
    } else {
        friendSection.classList.add("hidden");
        friendSection.querySelectorAll('input[type="checkbox"]').forEach(cb => cb.checked = false);
    }
}

function toggleEditFriendList() {
    const selectBox = document.getElementById("edit_accessLevelId");
    const friendSection = document.getElementById("edit_friendSelectSection");
    if (!selectBox || !friendSection) return;

    if (selectBox.value === "PROTECTED_1") {
        friendSection.classList.remove("hidden");
    } else {
        friendSection.classList.add("hidden");
    }
}


function handleSearch(){
	if(searchTimeout){
		clearTimeout(searchTimeout);
	}
	
	searchTimeout = setTimeout(() =>{
		loadPostViaAjax();
	}, 500);
}


function loadPostViaAjax() {
    const userId = (typeof serverOwnerId !== 'undefined') ? serverOwnerId : '';
	const searchInput = document.getElementById('searchInput');
	const keyword = searchInput ? searchInput.value.trim() : "";
    const url = `${contextPath}/dashboard-note?action=get-posts-html&_id=${userId}&search=${encodeURIComponent(keyword)}`;
    fetch(url)
        .then(response => {
            if (response.status === 401) {
                window.location.href = `${contextPath}/login`;
                throw new Error("Session expired");
            }
            return response.text();
        })
        .then(htmlFragment => {
            const container = document.querySelector('.notes-grid');
            if (container && htmlFragment.trim().length > 0) {
                container.innerHTML = htmlFragment;
            }else {
                container.innerHTML = '<p style="color: white; text-align: center; grid-column: 1/-1;">Không tìm thấy bài viết nào phù hợp.</p>';
            }
        })
        .catch(error => console.error(error));
}

function openEditModal(btn) {
    const id = btn.getAttribute('data-id');
    const topic = btn.getAttribute('data-topic');
    const access = btn.getAttribute('data-access');
    const comment = btn.getAttribute('data-comment');
    const viewersStr = btn.getAttribute('data-viewers') || "";

    let title = btn.getAttribute('data-title');
    let content = btn.getAttribute('data-content');
    
    const hiddenTitle = btn.querySelector('.hidden-data.title');
    const hiddenContent = btn.querySelector('.hidden-data.content');
    
    if(hiddenTitle) title = hiddenTitle.innerText;
    if(hiddenContent) content = hiddenContent.innerText;

    document.getElementById('edit_id').value = id;
    document.getElementById('edit_title').value = title;
    document.getElementById('edit_content').value = content;
    
    const topicInput = document.getElementById('edit_topicId'); 
    if(topicInput) topicInput.value = topic;
    
    const accessInput = document.getElementById('edit_accessLevelId');
    if(accessInput) accessInput.value = access;
    
    const commentInput = document.getElementById('edit_allowComment');
    if(commentInput) commentInput.value = comment;

    const checkboxes = document.querySelectorAll('#edit_friendSelectSection input[type="checkbox"]');
    checkboxes.forEach(cb => cb.checked = false);

    if (viewersStr) {
        const viewerArray = viewersStr.split(',');
        checkboxes.forEach(cb => {
            if (viewerArray.includes(cb.value)) {
                cb.checked = true;
            }
        });
    }
    
    toggleEditFriendList();

    const inputs = document.querySelectorAll('#editNoteModal input, #editNoteModal textarea');
    inputs.forEach(input => {
        if(input.value && input.value.trim() !== ""){
            input.classList.add('has-value');
        }
    });
    
    toggleModal('editNoteModal', true);
}

function switchSection(sectionName, clickedLink) {
    document.querySelectorAll('.menu-item').forEach(item => item.classList.remove('active'));
    if(clickedLink) clickedLink.classList.add('active');
    const topBar = document.querySelector('.top-bar');
    if(topBar) {
        topBar.style.display = (sectionName === 'wall') ? 'flex' : 'none';
    }

    const userId = (typeof serverOwnerId !== 'undefined') ? serverOwnerId : '';

    const url = `${contextPath}/dashboard-note?action=load-section&section=${sectionName}&_id=${userId}`;
    
    const container = document.getElementById('dynamic-content');
    container.innerHTML = '<div style="text-align:center; padding:20px; color:#666;"><i class="fa-solid fa-spinner fa-spin"></i> Loading...</div>';
    
    fetch(url)
        .then(response => {
            if(response.status === 401){
                window.location.href = `${contextPath}/login`;
                throw new Error("Session expired");
            }
            return response.text();
        })
        .then(htmlFragment => {
            container.innerHTML = htmlFragment;
        })
        .catch(error => {
            console.error(error);
            container.innerHTML = '<p style="color:red; text-align:center;">Lỗi tải dữ liệu.</p>';
        });
}

/*action*/
document.addEventListener("DOMContentLoaded", function () {
    const inputs = document.querySelectorAll('.form-group input, .form-group textarea, .input-wrapper input');

    function checkInput(input) {
        if (input.value && input.value.trim() !== "") {
            input.classList.add('has-value');
        } else {
            input.classList.remove('has-value');
        }
    }

    if (inputs.length > 0) {
        inputs.forEach(input => {
            checkInput(input);
            input.addEventListener('blur', function () { checkInput(this); });
            input.addEventListener('input', function () { checkInput(this); });
            input.addEventListener('animationstart', function (e) {
                if (e.animationName === "onAutoFillStart") checkInput(this);
            });
        });
    }

    const toggleBtns = document.querySelectorAll('.toggle-password');
    toggleBtns.forEach(btn => {
        btn.addEventListener('click', function () {
            const inputWrapper = this.parentElement;
            const passwordInput = inputWrapper.querySelector('input');
            if (passwordInput) {
                const isPass = passwordInput.type === 'password';
                passwordInput.type = isPass ? 'text' : 'password';
                this.classList.toggle('fa-eye', !isPass);
                this.classList.toggle('fa-eye-slash', isPass);
            }
        });
    });

    document.addEventListener('keydown', function (evt) {
        if (evt.key === "Escape" || evt.key === "Esc") {
            const openModals = document.querySelectorAll('.modal');
            openModals.forEach(modal => {
                if (modal.style.display === "block") toggleModal(modal.id, false);
            });
        }
    });

    if (document.querySelector('.notes-grid')) {
        loadPostViaAjax();
    }
});