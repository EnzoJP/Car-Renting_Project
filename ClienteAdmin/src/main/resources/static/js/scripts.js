/*!
    * Start Bootstrap - SB Admin v7.0.7 (https://startbootstrap.com/template/sb-admin)
    * Copyright 2013-2023 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */
//
// Scripts
//

window.addEventListener('DOMContentLoaded', () => {


    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }


    const excelLink = document.body.querySelector('#excelLink');
    if (excelLink) {
        excelLink.addEventListener('click', function(e) {
            // prevenir la acción por defecto para manejar la construcción de la URL
            e.preventDefault();

            // Pedir fechas con prompt; usuario puede cancelar para descargar todo
            var desde = prompt('Fecha desde (formato YYYY-MM-DD) - dejar vacío para sin filtro:');
            if (desde === null) {
                // usuario canceló -> no hacer nada
                return;
            }
            var hasta = prompt('Fecha hasta (formato YYYY-MM-DD) - dejar vacío para sin filtro:');
            if (hasta === null) {
                // usuario canceló -> no hacer nada
                return;
            }

            // Normalizar cadenas
            desde = desde.trim();
            hasta = hasta.trim();

            // Validaciones simples: si ambas vacías -> confirmar descarga de todo
            if (!desde && !hasta) {
                if (!confirm('¿Descargar todos los alquileres sin filtrar por fecha?')) {
                    return;
                }
            } else {
                // si una está vacía y la otra no, pedir completar
                if ((desde && !hasta) || (!desde && hasta)) {
                    alert('Por favor complete ambas fechas o deje ambas vacías.');
                    return;
                }
                // Validar formato YYYY-MM-DD con regex básico
                var re = /^\d{4}-\d{2}-\d{2}$/;
                if (desde && !re.test(desde)) { alert('Formato inválido en Fecha desde. Use YYYY-MM-DD.'); return; }
                if (hasta && !re.test(hasta)) { alert('Formato inválido en Fecha hasta. Use YYYY-MM-DD.'); return; }
                // Validar que desde <= hasta
                if (desde && hasta) {
                    var d1 = new Date(desde);
                    var d2 = new Date(hasta);
                    if (isNaN(d1.getTime()) || isNaN(d2.getTime())) { alert('Fechas inválidas.'); return; }
                    if (d1 > d2) { alert('La fecha "desde" no puede ser posterior a "hasta".'); return; }
                }
            }

            var url = excelLink.getAttribute('href') || '/generar-excel.xlsx';
            var params = [];
            if (desde) params.push('desde=' + encodeURIComponent(desde));
            if (hasta) params.push('hasta=' + encodeURIComponent(hasta));
            if (params.length) url += (url.indexOf('?') === -1 ? '?' : '&') + params.join('&');

            // Abrir en nueva pestaña para forzar la descarga
            window.open(url, '_blank');
        });
    }

});
