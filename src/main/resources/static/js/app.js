/**
 * Agenda de Contactos Pro - Application Logic (Vanilla JS)
 */

document.addEventListener('DOMContentLoaded', () => {

    // --- State Management ---
    let currentUser = JSON.parse(localStorage.getItem('agenda_user')) || null;
    let contactsList = [];
    let currentSelectedContact = null;

    // --- DOM Elements ---
    const authBanner = document.getElementById('auth-banner');
    const btnLoginModal = document.getElementById('btn-login-modal');
    const btnLogout = document.getElementById('btn-logout');
    const btnBannerLogin = document.getElementById('btn-banner-login');
    const userInfo = document.getElementById('user-info');
    const loggedUserName = document.getElementById('logged-user-name');

    const searchInput = document.getElementById('search-input');
    const btnClearSearch = document.getElementById('btn-clear-search');
    const btnNewContact = document.getElementById('btn-new-contact');

    const contactsGrid = document.getElementById('contacts-grid');
    const emptyState = document.getElementById('empty-state');
    const contactCounter = document.getElementById('contact-counter');

    // Modals
    const modalLogin = document.getElementById('modal-login');
    const formLogin = document.getElementById('form-login');
    const btnRegistroModal = document.getElementById('btn-registro-modal');
    const modalRegistro = document.getElementById('modal-registro');
    const formRegistro = document.getElementById('form-registro');
    const linkToRegister = document.getElementById('link-to-register');
    const linkToLogin = document.getElementById('link-to-login');

    const modalContacto = document.getElementById('modal-contacto');
    const formContacto = document.getElementById('form-contacto');
    const modalContactoTitle = document.getElementById('modal-contacto-title');
    const inputContactoId = document.getElementById('contacto-id');

    const modalFicha = document.getElementById('modal-ficha');
    const btnFichaBack = document.getElementById('btn-ficha-back');
    const btnFichaEdit = document.getElementById('btn-ficha-edit');
    const btnFichaDelete = document.getElementById('btn-ficha-delete');

    const modalDelete = document.getElementById('modal-delete');
    const deleteContactName = document.getElementById('delete-contact-name');
    const btnConfirmDelete = document.getElementById('btn-confirm-delete');

    // --- Init ---
    updateAuthUI();
    fetchContacts();

    // --- Event Listeners ---
    btnLoginModal.addEventListener('click', () => openModal(modalLogin));
    btnRegistroModal.addEventListener('click', () => openModal(modalRegistro));
    btnBannerLogin.addEventListener('click', () => openModal(modalLogin));
    btnLogout.addEventListener('click', handleLogout);

    document.querySelectorAll('.btn-close-login').forEach(b => b.addEventListener('click', () => closeModal(modalLogin)));
    document.querySelectorAll('.btn-close-registro').forEach(b => b.addEventListener('click', () => closeModal(modalRegistro)));
    document.querySelectorAll('.btn-close-contacto').forEach(b => b.addEventListener('click', () => closeModal(modalContacto)));
    document.querySelectorAll('.btn-close-ficha').forEach(b => b.addEventListener('click', () => closeModal(modalFicha)));
    document.querySelectorAll('.btn-close-delete').forEach(b => b.addEventListener('click', () => closeModal(modalDelete)));

    linkToRegister.addEventListener('click', (e) => {
        e.preventDefault();
        closeModal(modalLogin);
        openModal(modalRegistro);
    });

    linkToLogin.addEventListener('click', (e) => {
        e.preventDefault();
        closeModal(modalRegistro);
        openModal(modalLogin);
    });

    formLogin.addEventListener('submit', handleLogin);
    formRegistro.addEventListener('submit', handleRegistro);
    formContacto.addEventListener('submit', handleSaveContacto);
    btnNewContact.addEventListener('click', () => openContactoModal(null));

    // Botón dentro de la ficha "Volver a la lista"
    btnFichaBack.addEventListener('click', () => closeModal(modalFicha));

    // Botón dentro de la ficha "Editar"
    btnFichaEdit.addEventListener('click', () => {
        if (!currentUser) {
            showToast('Debes iniciar sesión para editar', 'error');
            openModal(modalLogin);
            return;
        }
        closeModal(modalFicha);
        openContactoModal(currentSelectedContact);
    });

    // Botón dentro de la ficha "Borrar"
    btnFichaDelete.addEventListener('click', () => {
        if (!currentUser) {
            showToast('Debes iniciar sesión para borrar', 'error');
            openModal(modalLogin);
            return;
        }
        closeModal(modalFicha);
        openDeleteModal(currentSelectedContact);
    });

    btnConfirmDelete.addEventListener('click', handleConfirmDelete);

    // Búsqueda en vivo
    searchInput.addEventListener('input', (e) => {
        const query = e.target.value.trim();
        if (query.length > 0) {
            btnClearSearch.classList.remove('hidden');
        } else {
            btnClearSearch.classList.add('hidden');
        }
        fetchContacts(query);
    });

    btnClearSearch.addEventListener('click', () => {
        searchInput.value = '';
        btnClearSearch.classList.add('hidden');
        fetchContacts();
    });

    // --- Authentication ---
    async function handleLogin(e) {
        e.preventDefault();
        const email = document.getElementById('login-email').value.trim();
        const password = document.getElementById('login-password').value.trim();

        try {
            const response = await fetch('/api/v1/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || 'Error al iniciar sesión');
            }

            currentUser = data;
            localStorage.setItem('agenda_user', JSON.stringify(currentUser));
            updateAuthUI();
            closeModal(modalLogin);
            showToast(`¡Bienvenido, ${currentUser.nombre}!`, 'success');
        } catch (error) {
            showToast(error.message, 'error');
        }
    }

    async function handleRegistro(e) {
        e.preventDefault();
        const nombre = document.getElementById('reg-nombre').value.trim();
        const email = document.getElementById('reg-email').value.trim();
        const password = document.getElementById('reg-password').value.trim();

        try {
            const response = await fetch('/api/v1/auth/registro', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nombre, email, password })
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || 'Error al registrar usuario');
            }

            currentUser = data;
            localStorage.setItem('agenda_user', JSON.stringify(currentUser));
            updateAuthUI();
            closeModal(modalRegistro);
            formRegistro.reset();
            showToast(`¡Cuenta creada con éxito! Bienvenido, ${currentUser.nombre}`, 'success');
        } catch (error) {
            showToast(error.message, 'error');
        }
    }

    function handleLogout() {
        currentUser = null;
        localStorage.removeItem('agenda_user');
        updateAuthUI();
        showToast('Sesión cerrada correctamente', 'info');
    }

    function updateAuthUI() {
        if (currentUser) {
            authBanner.classList.add('hidden');
            btnLoginModal.classList.add('hidden');
            btnRegistroModal.classList.add('hidden');
            btnLogout.classList.remove('hidden');
            userInfo.classList.remove('hidden');
            loggedUserName.textContent = currentUser.nombre;
            btnNewContact.disabled = false;
            btnNewContact.title = "Añadir nuevo contacto";

            btnFichaEdit.disabled = false;
            btnFichaDelete.disabled = false;
        } else {
            authBanner.classList.remove('hidden');
            btnLoginModal.classList.remove('hidden');
            btnRegistroModal.classList.remove('hidden');
            btnLogout.classList.add('hidden');
            userInfo.classList.add('hidden');
            btnNewContact.disabled = true;
            btnNewContact.title = "Inicia sesión para añadir contactos";

            btnFichaEdit.disabled = true;
            btnFichaDelete.disabled = true;
        }
    }

    // --- Fetch Contacts ---
    async function fetchContacts(query = '') {
        try {
            let url = '/api/v1/contactos?size=100';
            if (query) {
                url = `/api/v1/contactos/buscar?query=${encodeURIComponent(query)}&size=100`;
            }

            const response = await fetch(url);
            if (!response.ok) throw new Error('Error al cargar contactos');

            const data = await response.json();
            contactsList = data.content || [];

            renderContacts(contactsList);
        } catch (error) {
            showToast(error.message, 'error');
        }
    }

    // --- Render Contacts Grid ---
    function renderContacts(contacts) {
        contactsGrid.innerHTML = '';
        contactCounter.textContent = `${contacts.length} contacto${contacts.length === 1 ? '' : 's'}`;

        if (contacts.length === 0) {
            emptyState.classList.remove('hidden');
            return;
        }

        emptyState.classList.add('hidden');

        contacts.forEach(contact => {
            const card = document.createElement('div');
            card.className = 'contact-card';
            
            const inicial = contact.nombre ? contact.nombre.charAt(0).toUpperCase() : '?';

            card.innerHTML = `
                <div class="card-top">
                    <div class="avatar">${inicial}</div>
                    <div class="card-info">
                        <h3>${escapeHtml(contact.nombre)}</h3>
                        <span class="badge-provincia"><i class="fa-solid fa-location-dot"></i> ${escapeHtml(contact.provincia)}</span>
                    </div>
                </div>

                <div class="card-fields">
                    <div><i class="fa-solid fa-phone"></i> ${escapeHtml(contact.numero)}</div>
                    <div><i class="fa-solid fa-envelope"></i> ${escapeHtml(contact.email)}</div>
                </div>

                <div class="card-actions">
                    <button class="btn btn-primary btn-ficha-trigger" data-id="${contact.id}">
                        <i class="fa-solid fa-id-card"></i> Ver Ficha
                    </button>
                </div>
            `;

            // Botón "Ver Ficha" de la tarjeta
            card.querySelector('.btn-ficha-trigger').addEventListener('click', () => {
                openFichaModal(contact);
            });

            contactsGrid.appendChild(card);
        });
    }

    // --- Open Ficha Modal (Ver Ficha) ---
    function openFichaModal(contact) {
        currentSelectedContact = contact;

        document.getElementById('ficha-avatar').textContent = contact.nombre ? contact.nombre.charAt(0).toUpperCase() : '?';
        document.getElementById('ficha-nombre').textContent = contact.nombre;
        document.getElementById('ficha-provincia-badge').textContent = contact.provincia;

        document.getElementById('ficha-detail-nombre').textContent = contact.nombre;
        document.getElementById('ficha-detail-numero').textContent = contact.numero;
        document.getElementById('ficha-detail-email').textContent = contact.email;
        document.getElementById('ficha-detail-provincia').textContent = contact.provincia;

        const fechaFormat = contact.fechaCreacion ? new Date(contact.fechaCreacion).toLocaleString('es-ES') : 'Reciente';
        document.getElementById('ficha-detail-fecha').textContent = fechaFormat;

        openModal(modalFicha);
    }

    // --- Open Create / Edit Contact Modal ---
    function openContactoModal(contact = null) {
        formContacto.reset();

        if (contact) {
            modalContactoTitle.innerHTML = '<i class="fa-solid fa-pen-to-square"></i> Editar Contacto';
            inputContactoId.value = contact.id;
            document.getElementById('input-nombre').value = contact.nombre;
            document.getElementById('input-numero').value = contact.numero;
            document.getElementById('input-email').value = contact.email;
            document.getElementById('input-provincia').value = contact.provincia;
        } else {
            modalContactoTitle.innerHTML = '<i class="fa-solid fa-user-plus"></i> Registrar Contacto';
            inputContactoId.value = '';
        }

        openModal(modalContacto);
    }

    // --- Save (Create or Update) Contact ---
    async function handleSaveContacto(e) {
        e.preventDefault();
        const id = inputContactoId.value;
        const nombre = document.getElementById('input-nombre').value.trim();
        const numero = document.getElementById('input-numero').value.trim();
        const email = document.getElementById('input-email').value.trim();
        const provincia = document.getElementById('input-provincia').value.trim();

        const isEdit = !!id;
        const url = isEdit ? `/api/v1/contactos/${id}` : '/api/v1/contactos';
        const method = isEdit ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nombre, numero, email, provincia })
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || 'Error al guardar el contacto');
            }

            closeModal(modalContacto);
            showToast(isEdit ? 'Contacto actualizado' : 'Contacto registrado con éxito', 'success');
            fetchContacts();
        } catch (error) {
            showToast(error.message, 'error');
        }
    }

    // --- Delete Contact ---
    function openDeleteModal(contact) {
        currentSelectedContact = contact;
        deleteContactName.textContent = contact.nombre;
        openModal(modalDelete);
    }

    async function handleConfirmDelete() {
        if (!currentSelectedContact) return;

        try {
            const response = await fetch(`/api/v1/contactos/${currentSelectedContact.id}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                const data = await response.json();
                throw new Error(data.message || 'Error al eliminar el contacto');
            }

            closeModal(modalDelete);
            showToast('Contacto eliminado correctamente', 'success');
            fetchContacts();
        } catch (error) {
            showToast(error.message, 'error');
        }
    }

    // --- Helpers & Utilities ---
    function openModal(modal) {
        modal.classList.remove('hidden');
    }

    function closeModal(modal) {
        modal.classList.add('hidden');
    }

    function showToast(message, type = 'info') {
        const container = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        
        let icon = 'fa-circle-info';
        if (type === 'success') icon = 'fa-circle-check';
        if (type === 'error') icon = 'fa-circle-exclamation';

        toast.innerHTML = `<i class="fa-solid ${icon}"></i> <span>${escapeHtml(message)}</span>`;
        container.appendChild(toast);

        setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transform = 'translateX(100%)';
            setTimeout(() => toast.remove(), 300);
        }, 3500);
    }

    function escapeHtml(str) {
        if (!str) return '';
        return str.replace(/[&<>"']/g, function(m) {
            return {
                '&': '&amp;',
                '<': '&lt;',
                '>': '&gt;',
                '"': '&quot;',
                "'": '&#039;'
            }[m];
        });
    }
});
