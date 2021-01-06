//<page config>
$('#stock-table').DataTable({
      "paging": true,
      "lengthChange": false,
      "searching": false,
      "ordering": false,
      "info": false,
      "autoWidth": false,
      "responsive": false,
	  "pageLength": 5
    });
$('#stock-news-table').DataTable({
      "paging": true,
      "lengthChange": false,
      "searching": false,
      "ordering": false,
      "info": false,
      "autoWidth": false,
      "responsive": false,
	  "pageLength": 5
    });
$('#market-news-table').DataTable({
      "paging": true,
	  "pageLength": 5,
      "lengthChange": false,
      "searching": false,
      "ordering": false,
      "info": false,
      "autoWidth": false,
      "responsive": false
    });
//</page config>

angular.element(function() {
  angular.bootstrap(document, ['account','stock','market']);
});