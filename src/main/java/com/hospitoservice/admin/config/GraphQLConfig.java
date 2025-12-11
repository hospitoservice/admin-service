package com.hospitoservice.admin.config;

import com.hospitoservice.admin.model.Employee;
import com.hospitoservice.admin.repository.EmployeeRepository;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class GraphQLConfig {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Bean
    public GraphQLSchema graphQLSchema() throws IOException {
        // Load the schema file
        String schema = StreamUtils.copyToString(
                new ClassPathResource("graphql/schema.graphqls").getInputStream(),
                StandardCharsets.UTF_8
        );

        // Parse the schema
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(schema);
        RuntimeWiring runtimeWiring = buildRuntimeWiring();
        
        // Create the executable schema
        return new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("allEmployees", getAllEmployees())
                        .dataFetcher("employeeById", getEmployeeById())
                )
                .type("Mutation", typeWiring -> typeWiring
                        .dataFetcher("createEmployee", createEmployee())
                        .dataFetcher("updateEmployee", updateEmployee())
                        .dataFetcher("deleteEmployee", deleteEmployee())
                )
                .build();
    }

    private DataFetcher<Iterable<Employee>> getAllEmployees() {
        return dataFetchingEnvironment -> employeeRepository.findAll();
    }

    private DataFetcher<Employee> getEmployeeById() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            return employeeRepository.findById(id).orElse(null);
        };
    }

    private DataFetcher<Employee> createEmployee() {
        return dataFetchingEnvironment -> {
            Employee employee = dataFetchingEnvironment.getArgument("employee");
            return employeeRepository.save(employee);
        };
    }

    private DataFetcher<Employee> updateEmployee() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            Employee employee = dataFetchingEnvironment.getArgument("employee");
            employee.setId(id);
            return employeeRepository.save(employee);
        };
    }

    private DataFetcher<Boolean> deleteEmployee() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            employeeRepository.deleteById(id);
            return true;
        };
    }
}
