// Common Application Shell & UI Component Helpers
document.addEventListener('DOMContentLoaded', () => {
  renderNavbar();
  renderSidebar();
  updateUserBadges();
});

function renderNavbar() {
  const navContainer = document.getElementById('app-navbar');
  if (!navContainer) return;

  const user = Auth.getUser();
  const isAuth = !!user;
  const isAdmin = isAuth && user.roles && user.roles.includes('ROLE_ADMIN');

  let navRight = '';
  if (isAuth) {
    const avatar = user.avatarUrl || `https://ui-avatars.com/api/?name=${encodeURIComponent(user.fullName || 'User')}&background=4f46e5&color=fff`;
    navRight = `
      <div class="d-flex align-items-center gap-3">
        <span class="streak-badge d-none d-md-inline-flex" id="global-streak-badge" title="Daily Learning Streak">
          <i class="bi bi-fire text-danger"></i> <span id="global-streak-count">1</span> Day Streak
        </span>

        <div class="dropdown">
          <button class="btn btn-light rounded-pill border d-flex align-items-center gap-2 py-1 px-3" type="button" data-bs-toggle="dropdown">
            <img src="${avatar}" alt="Avatar" class="rounded-circle" width="28" height="28" style="object-fit: cover;">
            <span class="fw-semibold small d-none d-sm-inline">${user.fullName || 'Profile'}</span>
            <i class="bi bi-chevron-down small text-muted"></i>
          </button>
          <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 mt-2">
            <li class="px-3 py-2 border-bottom">
              <div class="fw-bold">${user.fullName}</div>
              <div class="small text-muted text-truncate" style="max-width: 180px;">${user.email}</div>
              ${isAdmin ? '<span class="badge bg-primary-subtle text-primary mt-1">Admin</span>' : '<span class="badge bg-secondary-subtle text-secondary mt-1">Learner</span>'}
            </li>
            <li><a class="dropdown-item py-2" href="dashboard.html"><i class="bi bi-speedometer2 me-2 text-primary"></i> Dashboard</a></li>
            <li><a class="dropdown-item py-2" href="profile.html"><i class="bi bi-person me-2 text-indigo"></i> My Profile</a></li>
            <li><a class="dropdown-item py-2" href="analytics.html"><i class="bi bi-graph-up me-2 text-success"></i> Analytics</a></li>
            <li><a class="dropdown-item py-2" href="recommendations.html"><i class="bi bi-stars me-2 text-warning"></i> AI Recommendations</a></li>
            ${isAdmin ? '<li><hr class="dropdown-divider"></li><li><a class="dropdown-item py-2 text-danger fw-semibold" href="admin-dashboard.html"><i class="bi bi-shield-lock me-2"></i> Admin Panel</a></li>' : ''}
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item py-2 text-danger" href="javascript:void(0)" onclick="Auth.logout()"><i class="bi bi-box-arrow-right me-2"></i> Logout</a></li>
          </ul>
        </div>
      </div>
    `;
  } else {
    navRight = `
      <div class="d-flex align-items-center gap-2">
        <a href="login.html" class="btn btn-outline-custom">Log In</a>
        <a href="register.html" class="btn btn-primary-custom">Get Started</a>
      </div>
    `;
  }

  navContainer.innerHTML = `
    <nav class="navbar navbar-expand-lg navbar-custom sticky-top">
      <div class="container-fluid px-lg-4">
        <div class="d-flex align-items-center gap-3">
          <button class="btn btn-light d-lg-none border-0 p-1" type="button" id="sidebar-toggle-btn" onclick="toggleSidebar()">
            <i class="bi bi-list fs-4"></i>
          </button>
          <a class="navbar-brand-custom" href="index.html">
            <i class="bi bi-mortarboard-fill fs-3 text-primary"></i>
            <span>QuizMaster</span>
          </a>
        </div>

        <div class="d-none d-lg-flex align-items-center gap-4 ms-4">
          <a href="categories.html" class="text-decoration-none text-secondary fw-semibold small ${isActive('categories.html')}">Categories</a>
          <a href="quizzes.html" class="text-decoration-none text-secondary fw-semibold small ${isActive('quizzes.html')}">Explore Quizzes</a>
          <a href="leaderboard.html" class="text-decoration-none text-secondary fw-semibold small ${isActive('leaderboard.html')}">Leaderboard</a>
          <a href="achievements.html" class="text-decoration-none text-secondary fw-semibold small ${isActive('achievements.html')}">Achievements</a>
        </div>

        <div class="ms-auto">
          ${navRight}
        </div>
      </div>
    </nav>
  `;
}

function renderSidebar() {
  const sidebarContainer = document.getElementById('app-sidebar');
  if (!sidebarContainer) return;

  const user = Auth.getUser();
  const isAdmin = user && user.roles && user.roles.includes('ROLE_ADMIN');
  const path = window.location.pathname;

  let links = `
    <div class="nav-heading">LEARNING PORTAL</div>
    <a href="dashboard.html" class="nav-link ${isActive('dashboard.html')}"><i class="bi bi-speedometer2"></i> Dashboard</a>
    <a href="quizzes.html" class="nav-link ${isActive('quizzes.html')}"><i class="bi bi-journal-check"></i> Available Quizzes</a>
    <a href="categories.html" class="nav-link ${isActive('categories.html')}"><i class="bi bi-grid-fill"></i> Subject Categories</a>
    <a href="recommendations.html" class="nav-link ${isActive('recommendations.html')}"><i class="bi bi-stars"></i> AI Recommendations</a>
    <a href="analytics.html" class="nav-link ${isActive('analytics.html')}"><i class="bi bi-graph-up-arrow"></i> Performance Analytics</a>
    <a href="leaderboard.html" class="nav-link ${isActive('leaderboard.html')}"><i class="bi bi-trophy"></i> Global Leaderboard</a>
    <a href="achievements.html" class="nav-link ${isActive('achievements.html')}"><i class="bi bi-award"></i> Badges & Rewards</a>
    <a href="profile.html" class="nav-link ${isActive('profile.html')}"><i class="bi bi-person-circle"></i> My Profile</a>
  `;

  if (isAdmin) {
    links += `
      <div class="nav-heading">ADMINISTRATION</div>
      <a href="admin-dashboard.html" class="nav-link ${isActive('admin-dashboard.html')}"><i class="bi bi-shield-check"></i> Admin Overview</a>
      <a href="admin-users.html" class="nav-link ${isActive('admin-users.html')}"><i class="bi bi-people-fill"></i> Manage Users</a>
      <a href="admin-categories.html" class="nav-link ${isActive('admin-categories.html')}"><i class="bi bi-folder-fill"></i> Manage Categories</a>
      <a href="admin-quizzes.html" class="nav-link ${isActive('admin-quizzes.html')}"><i class="bi bi-card-checklist"></i> Manage Quizzes</a>
      <a href="admin-questions.html" class="nav-link ${isActive('admin-questions.html')}"><i class="bi bi-patch-question-fill"></i> Manage Questions</a>
      <a href="admin-reports.html" class="nav-link ${isActive('admin-reports.html')}"><i class="bi bi-file-earmark-bar-graph"></i> Attempt Logs & Reports</a>
    `;
  }

  links += `
    <div class="mt-auto p-3 border-top border-secondary border-opacity-25">
      <a href="javascript:void(0)" onclick="Auth.logout()" class="nav-link text-danger"><i class="bi bi-box-arrow-right"></i> Logout</a>
    </div>
  `;

  sidebarContainer.innerHTML = `
    <aside class="sidebar" id="sidebar-nav">
      <div class="p-3 border-bottom border-secondary border-opacity-25 d-flex align-items-center justify-content-between">
        <span class="fw-bold text-white small"><i class="bi bi-layers-half text-primary me-2"></i> NAVIGATION</span>
        <button class="btn btn-sm btn-link text-light d-lg-none" onclick="toggleSidebar()"><i class="bi bi-x-lg"></i></button>
      </div>
      <nav class="d-flex flex-column h-100 pt-2">
        ${links}
      </nav>
    </aside>
  `;
}

function isActive(page) {
  return window.location.pathname.endsWith(page) ? 'active' : '';
}

function toggleSidebar() {
  const sb = document.getElementById('sidebar-nav');
  if (sb) sb.classList.toggle('show');
}

async function updateUserBadges() {
  if (!Auth.isAuthenticated()) return;
  try {
    const stats = await Api.get('/dashboard/stats');
    if (stats && stats.currentStreak !== undefined) {
      const streakEl = document.getElementById('global-streak-count');
      if (streakEl) streakEl.textContent = stats.currentStreak || 1;
    }
  } catch (e) {
    // optional silent fallback
  }
}
