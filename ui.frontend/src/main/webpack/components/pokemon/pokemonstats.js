const heightValues = document.querySelectorAll('.sub_barra');

heightValues.forEach(stat => {
    stat.style.height = `calc(${stat.getAttribute('data-height-value')} / 240 * 100%)`;
})