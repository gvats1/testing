package com.test.validreq

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
//import com.uptake.geotab.dto.ControllerResponse

@RestController
@RequestMapping("/rest/users")
class UsersResource(val usersRepository: UsersRepository, val geotabApiWrapper : GeotabApiWrapper) {

    @GetMapping(value = "/all")
    fun getUsers() = usersRepository.findAll()

    @GetMapping(value = "/insert/{name}")
    fun insertUsers(@PathVariable name: String): List<Users> {
        val users = Users(name)
        usersRepository.save(users)
        return usersRepository.findAll()
    }


    @PostMapping("/getEngineFault", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getEngineFault(@RequestBody reqBodyEngineFaultUrl: reqBodyEngineFaultUrl): ResponseEntity.BodyBuilder  {
        val url : String =reqBodyEngineFaultUrl.url;
        println(url);
        geotabApiWrapper.getEngineFaultData(reqBodyEngineFaultUrl,url);
        return ResponseEntity.accepted();


    }
}

@Entity
class Users(val name: String = "",
            val salary: Int = 2000,
            @Id
            @GeneratedValue(strategy = GenerationType.AUTO)
            val id: Long = 0)

interface UsersRepository : JpaRepository<Users, Long>