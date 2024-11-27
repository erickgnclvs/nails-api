#!/bin/bash

# Update entity references
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.model\./import com.nailservices.entity./g' {} +

# Update DTO references
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.dto\.NailService/import com.nailservices.dto.service.NailService/g' {} +
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.dto\.ServiceCategory/import com.nailservices.dto.service.ServiceCategory/g' {} +
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.dto\.Profile/import com.nailservices.dto.profile.Profile/g' {} +
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.dto\.Auth/import com.nailservices.dto.auth.Auth/g' {} +
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.dto\.Login/import com.nailservices.dto.auth.Login/g' {} +
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.dto\.Register/import com.nailservices.dto.auth.Register/g' {} +

# Update service references
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.service\.NailServiceManager/import com.nailservices.service.service.NailServiceService/g' {} +
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.service\.ServiceCategory/import com.nailservices.service.service.ServiceCategory/g' {} +
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.service\.Profile/import com.nailservices.service.profile.Profile/g' {} +
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.service\.Auth/import com.nailservices.service.auth.Auth/g' {} +
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.service\.CustomUserDetails/import com.nailservices.service.auth.CustomUserDetails/g' {} +

# Update controller references
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.controller\.NailService/import com.nailservices.controller.service.NailService/g' {} +
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.controller\.ServiceCategory/import com.nailservices.controller.service.ServiceCategory/g' {} +
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.controller\.Profile/import com.nailservices.controller.profile.Profile/g' {} +
find src/main/java -type f -name "*.java" -exec sed -i '' 's/import com\.nailservices\.controller\.Auth/import com.nailservices.controller.auth.Auth/g' {} +
