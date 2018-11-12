# RaceAI

Written in pure Java, this artificial intelligence is able to control a vehicle from point A to point B without human intervention. A genetic algorithm is used to calculate the direction.

## Idea

Our goal is to develop a program in which simulated vehicles learn to make decisions independently. We have equipped the vehicles with 5 sensors. The sensor angles are -90°, -45°, 0°, 45° and 90°. Each sensor measures the distance to the next wall and with the returned values the angle is calculated by which the vehicle has to turn.

Since it would take forever if only 1 vehicle at a time tried to reach the target, we release 200 vehicles at the same time.

## TODOs

* Implement JSON-Library to store best value
* Analyze and display learning data

## Getting Started

Not yet downloadable

### Installing

Not yet downloadable

## Authors

**Daniel Seifert** - *Genetic Algorithm* - [59Frames](https://github.com/59Frames)

**Cedric Girardin** - *Graphical Engine* - [Cedi-Code](https://github.com/cedi-code)

## Acknowledgments

* Genetic Algorithm inspired by Jabrils [Machine Learning Game](https://www.youtube.com/watch?v=ZX2Hyu5WoFg&t=364s)
* Neural Network based on Finn Eggers [Fully Connected](https://www.youtube.com/watch?v=d3OtgsGcMLw) youtube series

