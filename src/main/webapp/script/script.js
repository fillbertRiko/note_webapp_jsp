function checkInput() {
        document.querySelectorAll('.input-wrapper input').forEach(input => {
            if (input.value.trim() !== "") {
                input.classList.add('has-value');
            } else {
                input.classList.remove('has-value');
            }
        });
    }

    document.querySelectorAll('.input-wrapper input').forEach(input => {
        input.addEventListener('blur', checkInput);
        input.addEventListener('input', checkInput);
    });

    window.onload = checkInput;

/* dashboard-note*/
function openModal() {
	alert("Function create note has been build!");
} 