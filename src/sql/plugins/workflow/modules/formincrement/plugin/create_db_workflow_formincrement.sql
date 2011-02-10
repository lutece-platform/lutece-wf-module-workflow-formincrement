DROP TABLE IF EXISTS task_form_increment_cf;

/*==============================================================*/
/* Table structure for table tf_directory_cf					*/
/*==============================================================*/

CREATE TABLE task_form_increment_cf(
  id_task INT DEFAULT NULL,
  id_information INT DEFAULT NULL,
  PRIMARY KEY  (id_task)
  );
