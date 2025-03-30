if [[ -z "${BASE_URL}" ]]; then
    echo "BASE_URL is not set."
else
    k6 run -e BASE_URL=${BASE_URL} ./k6/performance_test.js
    exit 1
fi