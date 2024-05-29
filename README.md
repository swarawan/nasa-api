# NASA Asteroid API

This project is a Java Based Application that utilize NASA Asteroid API.

### Built with

---

- Java 17
- Spring Boot 3
- MySQL 8
- Docker
- Docker Compose

## Getting Started

--- 

### Prerequisites

Makes sure your machine is installed with below applications:

- Must be installed with Java 17. [Click here to download.](https://www.oracle.com/id/java/technologies/downloads/)
- Must be installed with MySQL 8. [Click here to download.](https://dev.mysql.com/downloads/installer/)
- Must be installed with Docker and Docker
  Compose  [Click here for the instruction.](https://docs.docker.com/engine/install/)

### NASA API Key

This project required to have a NASA API Key to run. Generate your own api key [here](https://api.nasa.gov/).
Then put it in `nasa-api` located in `src/main/resources/application.properties`.

## Features

### 1. Feed API

This API will return a list of Asteroid based on their closest approach date to Earth.

#### URL: http://localhost:8080/nasa/feed?start_date={}&end_date={}

#### Query Parameters

| Parameter  | Description                       | Format     | Default                              |
|------------|-----------------------------------|------------|--------------------------------------|
| start_date | Starting date for asteroid search | YYYY-MM-DD | none (required)                      |
| end_date   | End date for asteroid search      | YYYY-MM-DD | limited only 7 days after start_date |

#### Response

```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "date": "2023-01-01",
      "asteroids": [
        {
          "id": "2154347",
          "name": "154347 (2002 XK4)",
          "nasa_jpl_url": "https://ssd.jpl.nasa.gov/tools/sbdb_lookup.html#/?sstr=2154347",
          "absolute_magnitude": 16.08,
          "estimated_diameter_km": {
            "diameter_min": 1.0043982724,
            "diameter_max": 3.6144313359
          },
          "estimated_diameter_miles": {
            "diameter_min": 1.0043982724,
            "diameter_max": 2.2459028136
          },
          "estimated_diameter_feet": {
            "diameter_min": 5303.2246887282,
            "diameter_max": 11858.3709039515
          },
          "close_approaches": [
            {
              "date": "2023-01-01",
              "date_full": "2023-Jan-01 18:44",
              "velocity_kps": 27.3921993676,
              "velocity_kph": 98611.9177232095,
              "velocity_mph": 61273.6107652909,
              "distance_astronomical": 0.3312263376,
              "distance_lunar": 128.8470453264,
              "distance_kilometers": 49550754.592860915,
              "distance_miles": 30789411.180036105,
              "orbiting_body": "Earth"
            }
          ],
          "is_hazard_asteroid": false,
          "is_sentry_object": false
        }
      ]
    }
  ]
}
```

### 2. Single Feed API

This API will look up a specific Asteroid based on [NASA JPL](https://ssd.jpl.nasa.gov/tools/sbdb_query.html).

#### URL: localhost:8080/nasa/feed/{reference_id}

#### Path Parameters

| Parameter    | Description                                           | Format | Default  |
|--------------|-------------------------------------------------------|--------|----------|
| reference_id | Asteroid SPK-ID correlates to the NASA JPL Small Body | string | required |

#### Response
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": "2154347",
    "name": "154347 (2002 XK4)",
    "nasa_jpl_url": "https://ssd.jpl.nasa.gov/tools/sbdb_lookup.html#/?sstr=2154347",
    "absolute_magnitude": 16.08,
    "estimated_diameter_km": {
      "diameter_min": 1.0043982724,
      "diameter_max": 3.6144313359
    },
    "estimated_diameter_miles": {
      "diameter_min": 1.0043982724,
      "diameter_max": 2.2459028136
    },
    "estimated_diameter_feet": {
      "diameter_min": 5303.2246887282,
      "diameter_max": 11858.3709039515
    },
    "close_approaches": [
      {
        "date": "1904-07-07",
        "date_full": "1904-Jul-07 21:34",
        "velocity_kps": 19.5972016862,
        "velocity_kph": 70549.9260701702,
        "velocity_mph": 43836.9804517677,
        "distance_astronomical": 0.3082126677,
        "distance_lunar": 119.8947277353,
        "distance_kilometers": 46107958.5949378,
        "distance_miles": 28650156.945463665,
        "orbiting_body": "Earth"
      }
    ],
    "orbit_data": {
      "orbit_id": "194",
      "orbit_determination_date": "2024-05-02 05:51:41",
      "first_observation_date": "1998-02-22",
      "last_observation_date": "2024-05-01",
      "data_arc_in_days": 9565,
      "observations_used": 871,
      "orbit_uncertainty": 0,
      "minimum_orbit_intersection": 0.270085,
      "jupiter_tisserand_invariant": 3.632,
      "epoch_osculation": 2460400.5,
      "eccentricity": 0.6919283908383728,
      "semi_major_axis": 1.850196010709316,
      "inclination": 17.74644540809565,
      "ascending_node_longitude": 331.6478558600352,
      "orbital_period": 919.2317941403344,
      "perihelion_distance": 0.5699928622836423,
      "perihelion_argument": 24.98707114148122,
      "aphelion_distance": 3.13039915913499,
      "perihelion_time": 2460818.9856455806,
      "mean_anomaly": 196.1078964311179,
      "mean_motion": 0.3916313625081604,
      "equinox": "J2000",
      "orbit_class_type": "APO",
      "orbit_class_description": "Near-Earth asteroid orbits which cross the Earth’s orbit similar to that of 1862 Apollo",
      "orbit_class_range": "a (semi-major axis) > 1.0 AU; q (perihelion) < 1.017 AU"
    },
    "is_hazard_asteroid": false,
    "is_sentry_object": false
  }
}
```