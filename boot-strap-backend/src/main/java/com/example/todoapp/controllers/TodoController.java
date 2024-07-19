package com.example.todoapp.controllers;

import com.example.todoapp.models.Todo;
import com.example.todoapp.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class TodoController {

    @Autowired
    TodoRepository todoRepository;

     @GetMapping("/")
     public List<Todo> getAllTodos() {
         Sort sortByCreatedAtDesc = Sort.by(Sort.Direction.DESC, "createdAt");
         return todoRepository.findAll(sortByCreatedAtDesc);
     }

    @PostMapping("/api/create")
     public Todo createTodo( @RequestBody Todo todo) {
         todo.setCompleted(false);
         return todoRepository.save(todo);
    }

    @PutMapping(value="api/update/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable("id") String id,
                                            @RequestBody Todo todo) {
        return todoRepository.findById(id)
                .map(todoData -> {
                    todoData.setTitle(todo.getTitle());
                    todoData.setCompleted(todo.getCompleted());
                    Todo updatedTodo = todoRepository.save(todoData);
                    return ResponseEntity.ok().body(updatedTodo);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value="api/delete/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") String id) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todoRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}