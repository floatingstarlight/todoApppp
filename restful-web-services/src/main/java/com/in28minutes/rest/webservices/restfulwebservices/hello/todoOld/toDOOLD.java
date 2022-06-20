package com.in28minutes.rest.webservices.restfulwebservices.hello.todoOld;

import com.in28minutes.rest.webservices.restfulwebservices.todo.Todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class toDOOLD {
  @RestController
  @CrossOrigin(origins="http://localhost:4200")
  public static class TodoResource {
    @Autowired
    private TodoHardcodedService todoService;

    //Get all to-do objects
    @GetMapping("/users/{username}/todos")
    public List<Todo> getAllTodos(@PathVariable String username) {
      return todoService.findAll();
    }

    //Get one specific to-do object
    @GetMapping("/users/{username}/todos/{id}")
    public Todo getTodo(@PathVariable String username, @PathVariable long id) {
      return todoService.findById(id);
    }

    //Update todo
    @PutMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String username, @PathVariable long id, @RequestBody Todo todo) {
      Todo todoUpdated = todoService.save(todo);
      return new ResponseEntity<Todo>(todo, HttpStatus.OK);
    }

    //Create a new todo
    //Get current resource url
    @PostMapping("/users/{username}/todos")
    public ResponseEntity<Void> updateTodo(@PathVariable String username, @RequestBody Todo todo) {
      Todo createdTodo = todoService.save(todo);

      URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdTodo.getId()).toUri();
      return ResponseEntity.created(uri).build();
    }





    //Delete todo
    @DeleteMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String username, @PathVariable long id) {
      Todo todo = todoService.deleteById(id);
      if (todo != null) { //responseEntity helps us build specific requests with specific state assigned
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.notFound().build();
    }

  }

  @Service
  public static class TodoHardcodedService {
    private static List<Todo> todos = new ArrayList<>();
    private static long idCounter = 0;
    static {
      todos.add(new Todo(++idCounter, "in28minutes", "Learn to dance", new Date(), false));
      todos.add(new Todo(++idCounter, "in28minutes", "Learn React", new Date(), false));
      todos.add(new Todo(++idCounter, "in28minutes", "Learn Leetcode", new Date(), false));
    }

    public List<Todo> findAll() {
      return todos;
    }


    public Todo deleteById(long id) {
      Todo todo = findById(id);

      if (todo == null) return null;
      if (todos.remove(todo)) return todo;
      return null;
    }


    public Todo findById(long id) {
      for (Todo todo : todos) {
        if (todo.getId() == id) return todo;
      }
      return null;
    }

    public Todo save(Todo todo) {
      if (todo.getId() == -1 || todo.getId() == 0) {
        todo.setId(++idCounter);
        todos.add(todo);
      } else {
        deleteById(todo.getId());
        todos.add(todo);
      }
      return todo;
    }


  }
}
