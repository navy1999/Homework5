#!/bin/bash

#!/bin/bash

# Array of set types
set_types=("CoarseSet" "FineSet" "LazySet" "LockFreeSet" "OptimisticSet")

# Array of thread counts
thread_counts=(4 8 12 16 20 24 28 32 36 40)

# Array of contains percentages
contains_percentages=(20 40 60 80)

# Loop through all configurations
for set_type in "${set_types[@]}"; do
    for thread_count in "${thread_counts[@]}"; do
        for contains_percentage in "${contains_percentages[@]}"; do
            echo "Running benchmark for $set_type with $thread_count threads and $contains_percentage% contains operations"
            
            # Convert percentage to decimal
            contains_decimal=$(echo "scale=2; $contains_percentage" | bc)
            
            # Run the Java benchmark
            java -cp build/libs/homework5.jar edu.vt.ece.hw5.Benchmark "$set_type" "$thread_count" "$contains_decimal"
            
            echo "----------------------------------------"
        done
    done
done

echo "All benchmarks completed."
