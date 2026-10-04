// QuizMaster API Client & Auth Manager
const API_BASE = '/api';

const Auth = {
  getToken() {
    return localStorage.getItem('qm_token');
  },
  getUser() {
    const u = localStorage.getItem('qm_user');
    return u ? JSON.parse(u) : null;
  },
  setAuth(token, user) {
    localStorage.setItem('qm_token', token);
    localStorage.setItem('qm_user', JSON.stringify(user));
  },
  clearAuth() {
    localStorage.removeItem('qm_token');
    localStorage.removeItem('qm_user');
  },
  isAuthenticated() {
    return !!this.getToken();
  },
  hasRole(role) {
    const user = this.getUser();
    return user && user.roles && user.roles.includes(role);
  },
  requireAuth() {
    if (!this.isAuthenticated()) {
      window.location.href = 'login.html?redirect=' + encodeURIComponent(window.location.pathname + window.location.search);
      return false;
    }
    return true;
  },
  requireAdmin() {
    if (!this.requireAuth()) return false;
    if (!this.hasRole('ROLE_ADMIN')) {
      alert('Access Denied: Administrator role required.');
      window.location.href = 'dashboard.html';
      return false;
    }
    return true;
  },
  logout() {
    this.clearAuth();
    window.location.href = 'login.html';
  }
};

const Api = {
  async request(endpoint, options = {}) {
    const url = endpoint.startsWith('http') ? endpoint : `${API_BASE}${endpoint.startsWith('/') ? '' : '/'}${endpoint}`;
    const headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      ...options.headers
    };

    const token = Auth.getToken();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    try {
      const response = await fetch(url, {
        ...options,
        headers
      });

      if (response.status === 401) {
        if (!endpoint.includes('/auth/login') && !endpoint.includes('/auth/register')) {
          Auth.clearAuth();
          window.location.href = 'login.html';
          return null;
        }
      }

      const contentType = response.headers.get('content-type');
      let data = null;
      if (contentType && contentType.includes('application/json')) {
        data = await response.json();
      } else {
        data = await response.text();
      }

      if (!response.ok) {
        const errorMsg = (data && data.message) ? data.message : (typeof data === 'string' && data ? data : `Error ${response.status}`);
        throw new Error(errorMsg);
      }

      return data;
    } catch (err) {
      console.error(`API Error [${endpoint}]:`, err);
      throw err;
    }
  },

  get(endpoint) {
    return this.request(endpoint, { method: 'GET' });
  },

  post(endpoint, body) {
    return this.request(endpoint, {
      method: 'POST',
      body: JSON.stringify(body)
    });
  },

  put(endpoint, body) {
    return this.request(endpoint, {
      method: 'PUT',
      body: JSON.stringify(body)
    });
  },

  del(endpoint) {
    return this.request(endpoint, { method: 'DELETE' });
  }
};

// UI Notification Toasts
function showToast(message, type = 'success') {
  if (typeof Swal !== 'undefined') {
    const Toast = Swal.mixin({
      toast: true,
      position: 'top-end',
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true
    });
    Toast.fire({
      icon: type,
      title: message
    });
  } else {
    alert(message);
  }
}
