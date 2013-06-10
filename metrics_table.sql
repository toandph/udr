CREATE  TABLE `metrics`.`metrics_directory` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `entity_id` INT NULL ,

  `directory_size_bytes` INT NULL ,
  `lines_in_directory` INT NULL ,
  `lines_in_source_code` INT NULL ,
  
  `number_of_statements` INT NULL ,
  `lines_of_comment` INT NULL ,
  `comment_ratio` FLOAT NULL ,
  
  `total_files` INT NULL ,
  `cpp_files` INT NULL ,
  `c_files` INT NULL ,
  
  `header_files` INT NULL ,
  `java_files` INT NULL ,
  `java_class_files` INT NULL ,
  
  `variable_in_directory` INT NULL ,
  `declaration_in_directory` INT NULL ,
  `functions_in_directory` INT NULL ,
  
  `total_cyclo_complex` INT NULL ,
  `ave_cyclo_complex` FLOAT NULL ,
  `max_cyclo_complex` INT NULL ,
  
  `program_difficulty` INT NULL ,
  `intelligent_content` INT NULL ,
  `program_volume` INT NULL ,
  
  `mental_effort` INT NULL ,
  `maintainability_index` INT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `entity_id_UNIQUE` (`entity_id` ASC) );
  

CREATE  TABLE `metrics`.`metrics_file` (

  `id` INT NOT NULL AUTO_INCREMENT ,

  `entity_id` INT NULL ,

    `comment_ratio` FLOAT NULL ,
    `intelligent_content` INT NULL ,
    `mental_effort` INT NULL ,
	
    `program_difficulty` INT NULL ,
    `program_volume` INT NULL ,
    `directly_included_files` INT NULL, 
	
    `files_where_dir_incl` INT NULL,
    `files_where_trans_incl` INT NULL,
    `trans_included_files` INT NULL,
	
    `trans_included_lines` INT NULL,
    `maintainability_index` INT NULL ,
    `ave_cyclo_complex` FLOAT NULL ,
	
    `max_cyclo_complex` INT NULL ,
    `total_cyclo_complex` INT NULL ,
    `declaration_in_file` INT NULL ,
	
    `functions_in_file` INT NULL ,
    `variable_in_file` INT NULL ,
    `file_size_bytes` INT NULL ,
	
    `lines_in_file` INT NULL ,
    `lines_of_comment` INT NULL ,
    `lines_in_source_code` INT NULL ,
	
    `number_of_statements` INT NULL ,
  
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `entity_id_UNIQUE` (`entity_id` ASC) );
  
  
  CREATE  TABLE `metrics`.`metrics_namespace` (

  `id` INT NOT NULL AUTO_INCREMENT ,

  `entity_id` INT NULL ,

    `member_attributes` FLOAT NULL ,
    `member_classes` INT NULL ,
    `member_methods` INT NULL ,
    `total_members` INT NULL ,
    
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `entity_id_UNIQUE` (`entity_id` ASC) );
  
  CREATE  TABLE `metrics`.`metrics_class` (

  `id` INT NOT NULL AUTO_INCREMENT ,

  `entity_id` INT NULL ,

    `coupling_between_objects` FLOAT NULL ,
    `depth_of_inheritance_tree` INT NULL ,
    `lack_of_cohesion` INT NULL ,
	
    `number_of_children` INT NULL ,
    `response_for_a_class` INT NULL ,
    `weighted_methods` INT NULL, 
	
    `hierarchy_and_vars_coupling` INT NULL,
    `derived_class_depth` INT NULL,
    `external_methods_and_coupling` INT NULL,
	
    `fan_in_of_inherited_classes` INT NULL,
    `intelligent_content` INT NULL ,
    `mental_effort` INT NULL ,
	
    `program_difficulty` INT NULL ,
    `program_volume` INT NULL ,
    `ave_cyclo_complex` FLOAT NULL ,
	
    `max_cyclo_complex` INT NULL ,
    `methods_called_external` INT NULL ,
    `methods_called_internal` INT NULL ,
	
    `member_attributes` INT NULL ,
    `member_classes` INT NULL ,
    `member_methods` INT NULL ,
	
    `private_methods` INT NULL ,
    `member_types` INT NULL ,
    `total_members` INT NULL ,
    
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `entity_id_UNIQUE` (`entity_id` ASC) );
  
  CREATE  TABLE `metrics`.`metrics_function` (

  `id` INT NOT NULL AUTO_INCREMENT ,

  `entity_id` INT NULL ,

    `decision_depth` FLOAT NULL ,
    `fan_in` INT NULL ,
    `transitive_fan_in` INT NULL ,
	
    `fan_out` INT NULL ,
    `transitive_fan_out` INT NULL ,
    `intelligent_content` INT NULL, 
	
    `mental_effort` INT NULL,
    `program_difficulty` INT NULL,
    `program_volume` INT NULL,
	
    `cyclomatic_complexity` INT NULL,
    `decision_density` INT NULL ,
    `essential_complexity` INT NULL ,
	
    `essential_density` INT NULL ,
    `parameters` INT NULL ,
    `returns` FLOAT NULL ,
	
    `variables_in_function` INT NULL ,
    `coverage` INT NULL ,
    `frequency` INT NULL ,
	
    `time` INT NULL ,
    `lines_in_function` INT NULL ,
    `lines_of_source_code` INT NULL ,
	
    `global_vars_used` INT NULL ,
    `static_vars_used` INT NULL ,
    
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `entity_id_UNIQUE` (`entity_id` ASC) );
  
  CREATE  TABLE `metrics`.`metrics_variable` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `entity_id` INT NULL ,
    `functions_reading` INT NULL ,
    `functions_setting` INT NULL ,
    `functions_using` INT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `entity_id_UNIQUE` (`entity_id` ASC) );
  
  CREATE  TABLE `metrics`.`metrics_project` (

  `id` INT NOT NULL AUTO_INCREMENT ,

  `entity_id` INT NULL ,

    `project_size_kbytes` FLOAT NULL ,
    `lines_in_project` INT NULL ,
    `liness_of_source_code` INT NULL ,
	
    `number_of_statement` INT NULL ,
    `lines_of_comments` INT NULL ,
    `comment_ratio` FLOAT NULL, 
	
    `cpp_files` INT NULL,
    `c_files` INT NULL,
    `header_files` INT NULL,
	
    `java_files` INT NULL,
    `java_class_files` INT NULL ,
    `namespace` INT NULL ,
	
    `total_classes` INT NULL ,
    `classes` INT NULL ,
    `template` FLOAT NULL ,
	
    `struct_unions` INT NULL ,
    `functions` INT NULL ,
    `types` INT NULL ,
	
    `defined_functions` INT NULL ,
    `library_functions` INT NULL ,
    `variables` INT NULL ,
	
    `non_local_variables` INT NULL ,
    `local_variables` INT NULL ,
    `macros` INT NULL ,
    
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `entity_id_UNIQUE` (`entity_id` ASC) );
  
  CREATE  TABLE `metrics`.`entity` (

  `id` INT NOT NULL ,
  `path` VARCHAR(2500) NULL ,
  `owner` VARCHAR(2500) NULL ,
  `permission` VARCHAR(45) NULL ,
  `mod_date` DATETIME NULL ,
  `size` INT NULL ,
  `category` VARCHAR(45) NULL ,
  `base_class` VARCHAR(2500) NULL ,
  `scope` VARCHAR(45) NULL ,
  `kind` VARCHAR(45) NULL ,
  `defined` VARCHAR(2500) NULL ,
  `type` VARCHAR(45) NULL ,
  PRIMARY KEY (`id`) );
  
  


  
  
    