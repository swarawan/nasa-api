create table if not exists `sentry_data`
(
    `id`                          bigint primary key auto_increment not null,
    `spk_id`                      varchar(256)                      not null,
    `designation`                 varchar(256)                      not null,
    `sentry_id`                   varchar(256),
    `name`                        varchar(256),
    `year_range_min`              int,
    `year_range_max`              int,
    `potential_impact`            int,
    `impact_probability`          varchar(100),
    `v_infinite`                  double,
    `absolute_magnitude`          double,
    `estimated_diameter`          double,
    `palermo_scale_ave`           double,
    `palermo_scale_max`           double,
    `torino_scale`                int,
    `last_obs`                    date,
    `last_obs_jd`                 double,
    `is_active_sentry_object`     tinyint,
    `impact_details_url`          text,
    `orbital_element_details_url` text,
    `created_at`                  datetime                          not null default current_timestamp,
    `updated_at`                  datetime on update current_timestamp,
    `deleted_at`                  datetime
);

create table if not exists `asteroid_data`
(
    `id`                  bigint primary key auto_increment not null,
    `reference_id`        varchar(256)                      not null,
    `name`                varchar(256)                      not null,
    `nasa_jpl_url`        varchar(256),
    `approach_date`       datetime,
    `absolute_magnitude`  double,
    `diameter_km_min`     double,
    `diameter_km_max`     double,
    `diameter_miles_min`  double,
    `diameter_miles_max`  double,
    `diameter_feet_min`   double,
    `diameter_feet_max`   double,
    `is_hazard_potential` tinyint,
    `is_sentry_object`    tinyint,
    `created_at`          datetime                          not null default current_timestamp,
    `updated_at`          datetime on update current_timestamp,
    `deleted_at`          datetime
);

create table if not exists `close_approach`
(
    `id`                    bigint primary key auto_increment not null,
    `reference_id`          varchar(256),
    `approach_date`         datetime,
    `approach_date_full`    varchar(100),
    `approach_date_epoch`   bigint,
    `velocity_kps`          double,
    `velocity_kph`          double,
    `velocity_mph`          double,
    `distance_astronomical` double,
    `distance_lunar`        double,
    `distance_kilometers`   double,
    `distance_miles`        double,
    `orbiting_body`         varchar(100),
    `created_at`            datetime                          not null default current_timestamp,
    `updated_at`            datetime on update current_timestamp,
    `deleted_at`            datetime
);


