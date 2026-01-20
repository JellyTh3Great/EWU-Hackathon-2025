# NetTrace

NetTrace is a Java-based network analysis and visualization tool originally developed during a hackathon.  
The project focuses on tracing network paths, resolving DNS information, and performing basic IP geolocation to help visualize how traffic moves across the internet.

This repository contains the original project files as they existed at the conclusion of the hackathon.

This project landed me 3rd place as a solo team.
---

## Project Overview

NetTrace combines several core networking concepts into a single application:

- Traceroute execution and hop tracking
- DNS resolution for hostnames
- IP geolocation lookup
- A simple user interface for displaying results

The goal of the project was to provide an educational and exploratory tool that demonstrates how packets traverse networks and how public IP metadata can be correlated with routing paths.

---

## Features

- **Traceroute Execution**
  - Executes traceroute logic and records individual hops
  - Stores hop details such as IP addresses and response order

- **DNS Resolution**
  - Resolves hostnames to IP addresses before tracing

- **IP Geolocation**
  - Performs basic geographic lookup for IP addresses
  - Helps visualize approximate routing paths

- **User Interface**
  - Simple Java-based UI for interacting with the tool and viewing results

---

## Project Structure

All project files are located in a single directory for simplicity:

- `NetTraceApp.class` – Main application entry point  
- `TracerouteRunner.class` – Handles traceroute execution  
- `TracerouteHop.class` – Represents individual traceroute hops  
- `DNSResolver.class` – Performs DNS lookups  
- `IPGeoLocator.class` – Handles IP geolocation logic  
- `UI.class`, `UI$1.class` – User interface components  

> Note: This repository contains compiled `.class` files as preserved from the original hackathon submission.

---

## How It Works (High-Level)

1. User provides a target hostname or IP address
2. DNS resolution is performed (if applicable)
3. Traceroute is executed hop-by-hop
4. Each hop is recorded and optionally geolocated
5. Results are displayed through the UI

This mirrors real-world network diagnostic workflows used in networking and cybersecurity contexts.

---

## Requirements

- Java Runtime Environment (JRE) 8 or newer
- Network access (for traceroute, DNS, and geolocation queries)

---

## Running the Project

Because this repository contains compiled `.class` files, execution assumes a compatible Java environment.

Example:
```bash
java NetTraceApp
