# IntegersGrouping

This program implements an adaptation of the k-means algorithm to one dimension and groups numbers in n groups depending on how close they are to each other.
* It starts by assigning n random numbers from the initial array as the centers of each group.
* After that calculates the distance between each point and the centers and assigns the points to the group whose center is the closest.
* After all points are assigned the centers of the groups are recalculated as the mean of all the points of each center and the algorithm runs again until the centers stops changing.

This algorithm tends to be unconsistent because of the initial random assign of the centers. So in this implementation the algorithm runs n specified times (by default 50) and the solution is the best result. The best result is the one with the lowest summation of the squares of the distances of each point to their center. The square is used in order to penalize more integer that are more further from the center.
