function initPokemonStats() {
  document.querySelectorAll('.pokemon-stats-info .meter').forEach(function (meter) {
      const value = meter.getAttribute('data-value');
      const topSpace = value ? 100 - (value * 100 / 240) : 100;
      meter.style.top = topSpace + '%';
    }
  );
}

if (document.readyState !== 'loading') {
  initPokemonStats();
} else {
  document.addEventListener('DOMContentLoaded', initPokemonStats);
}
