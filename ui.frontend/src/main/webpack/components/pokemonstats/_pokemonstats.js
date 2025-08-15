document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.stat-column').forEach(col => {
      const val = col.dataset.value;
      col.style.setProperty('--value', val);
    });
  });