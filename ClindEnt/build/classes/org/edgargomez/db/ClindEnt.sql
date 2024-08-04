-- Nombre: Edgar Edwin Alexander Gómez García.
-- Curso: Taller, Tecnología, Dibujo y Calcúlo.
-- Codigo Tecnico: 2021400
-- Fecha de Creación: 06/05/2022
-- Fecha de Modificación: 06/05/22

Drop database if exists ClindEntIN5CV2_2021400;
Create database ClindEntIN5CV2_2021400;

Use ClindEntIN5CV2_2021400;

Create table Pacientes(
	codigoPaciente int not null,
    nombresPaciente varchar (50) not null,
    apellidosPacientes varchar (50) not null,
    sexo varchar (50) not null,
    fechaNacimiento date not null,
    direccionPaciente varchar (50) not null,
    telefonoPersonal varchar (8) not null,
    fechaPrimeraVisita date,
    primary key PK_codigoPaciente (codigoPaciente)
);

Create table Especialidades(
	codigoEspecialidad int not null auto_increment,
    descripcion varchar (100) not null,
    primary key PK_codigoEspecialidad (codigoEspecialidad)
);

Create table Medicamento(
	codigoMedicamento int not null auto_increment,
    nombreMedicamento varchar (50) not null,
    primary key PK_codigoMedicamento (codigoMedicamento)
);

Create table Doctores(    
	numeroColegiado int not null,
	nombresDoctores varchar (50) not null,
    apellidosDoctor varchar (50) not null,
    telefonoContacto varchar (8) not null,
    codigoEspecialidad int not null,
    primary key PK_numeroColegiado (numeroColegiado),
    constraint FK_Doctores_Especialidades foreign key (codigoEspecialidad)
		references Especialidades (codigoEspecialidad)
);

Create table Recetas(               
	codigoReceta int not null auto_increment,
    fechaReceta date not null,
    numeroColegiado int not null,
    primary key PK_codigoReceta (codigoReceta),
    constraint FK_Recetas_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);

Create table DetalleReceta(            
	CodigoDetalleReceta int not null auto_increment,
    dosis varchar (100) not null,
    codigoReceta int not null,
    codigoMedicamento int not null,
    primary key PK_codigoDetalleReceta (codigoDetalleReceta),
    constraint FK_DetalleReceta_Recetas foreign key (codigoReceta)
		references Recetas (codigoReceta),
	constraint FK_DetalleReceta_Medicamento foreign key (codigoMedicamento)
		references Medicamento (codigoMedicamento)
);

Create table Citas (
	codigoCita int not null auto_increment,
	fechaCita date not null,
	horaCita time not null,
	tratamiento varchar(150),
	descripcionCondActual varchar(255) not null,
	codigoPaciente int not null,
	numeroColegiado int not null,
	primary key PK_codigoCita(codigoCita),
	constraint FK_Citas_Pacientes foreign key (codigoPaciente)
	references Pacientes(codigoPaciente),
	constraint FK_Citas_Doctores foreign key (numeroColegiado)
	references Doctores (numeroColegiado)
);

-- ------------------------------------------------------------------------------------------------------
-- -------------- PACIENTES -----------------------------------------------
-- --------------AGREGAR PACIENTE------------------------------------------

Delimiter $$
	create procedure sp_AgregarPaciente (in codigoPaciente int, in nombresPaciente varchar(50), 
	in apellidosPacientes varchar(50), in sexo char, in fechaNacimiento date,
	in direccionPaciente varchar(50), in telefonoPersonal varchar(8), in fechaPrimeraVisita date)
		Begin
			Insert into Pacientes (codigoPaciente, nombresPaciente, apellidosPacientes, sexo, fechaNacimiento, 
									direccionPaciente, telefonoPersonal, fechaPrimeraVisita)
							Values (codigoPaciente, nombresPaciente, apellidosPacientes, upper(sexo), fechaNacimiento, 
									direccionPaciente, telefonoPersonal, fechaPrimeraVisita);                 
        End$$
Delimiter ;

call sp_AgregarPaciente (2, 'Edgar Edwin', 'Gómez García', 'm', '2004-11-04', 'zona 6 Chinautla', '4728813', now());
call sp_AgregarPaciente (5, 'Aura Josefina', 'García Estrada', 'f', '1970-10-31', 'Mazatenango', '51974290', now());

-- ------------------------------LISTAR PACIENTES------------------------------ --
Delimiter $$
	create procedure sp_ListarPacientes()
		Begin
			select
				P.codigoPaciente, 
                P.nombresPaciente, 
                P.apellidosPacientes, 
                P.sexo, 
                P.fechaNacimiento, 
                P.direccionPaciente, 
                P.telefonoPersonal, 
                P.fechaPrimeraVisita
            From Pacientes P;
        End$$
Delimiter ;

call sp_ListarPacientes();

-- ------------------------------BUSCAR PACIENTES------------------------------ --
Delimiter $$
	create procedure sp_BuscarPaciente(in codPaciente int)
		Begin
			select
            P.codigoPaciente, 
            P.nombresPaciente, 
            P.apellidosPacientes, 
            P.sexo, 
            P.fechaNacimiento, 
            P.direccionPaciente, 
            P.telefonoPersonal, 
            P.fechaPrimeraVisita
            from Pacientes P where codigoPaciente = codPaciente;
        End$$
Delimiter ;

call sp_BuscarPaciente(2);

-- ------------------------------ELIMINAR PACIENTE------------------------------ --
Delimiter $$
	create procedure sp_EliminarPaciente(in codPaciente int)
		Begin
			Delete from Pacientes
				Where codigoPaciente = codPaciente;
        End$$
Delimiter ;

call sp_EliminarPaciente(5);

-- ------------------------------EDITAR PACIENTE------------------------------ --
Delimiter $$
	create procedure sp_EditarPaciente(in codPaciente int, in nomPaciente varchar(50), in apPaciente varchar(50),
		in sx char, in fechaNac date, in dirPaciente varchar(50), in telPersonal varchar(50), in fechaPV date)
		Begin
			update Pacientes P
            set P.nombresPaciente = nomPaciente, 
				P.apellidosPacientes = apPaciente, 
                P.sexo = sx, 
                P.fechaNacimiento = fechaNac,
				P.direccionPaciente = dirPaciente, 
                P.telefonoPersonal = telPersonal, 
                P.fechaPrimeraVisita = fechaPV
				Where codigoPaciente = codPaciente;
        End$$
Delimiter ;

call sp_EditarPaciente(2, 'Cristian Neftali', 'Gómez García', 'M', '2008-08-10', 'Zona 6 Chinautla', '33459868', now());

-- -------------- ESPECIALIDADES -----------------------------------------------

-- ------------------------------AGREGAR ESPECIALIDADES------------------------------ --
Delimiter $$
	create procedure sp_AgregarEspecialidade(in descripcion varchar(100))
		Begin
			Insert into Especialidades(descripcion) values(descripcion);
        End$$
Delimiter ;

call sp_AgregarEspecialidade('brackets superiores');
call sp_AgregarEspecialidade('Relleno de muela');

-- ------------------------------LISTAR ESPECIALIDADES------------------------------ --
Delimiter $$
	create procedure sp_ListarEspecialidades()
		Begin
			select
            E.codigoEspecialidad, 
            E.descripcion
            from Especialidades E;
        End$$
Delimiter ;

call sp_ListarEspecialidades();

-- ------------------------------BUSCAR ESPECIALIDADES------------------------------ --
Delimiter $$
	create procedure sp_BuscarEspecialidade(in codEs int)
		Begin
			select
            E.codigoEspecialidad, 
            E.descripcion
            from Especialidades E where codigoEspecialidad = codEs;
        End$$
Delimiter ;

-- call sp_BuscarEspecialidade(1);

-- ------------------------------ELIMINAR ESPECIALIDADES------------------------------ --
Delimiter $$
	create procedure sp_EliminarEspecialidade(in codEs int)
		Begin
			Delete from Especialidades
				where codigoEspecialidad = codEs;
        End$$
Delimiter ;

-- call sp_EliminarEspecialidade(2);

-- ------------------------------EDITAR ESPECIALIDADES ------------------------------ --
Delimiter $$
	create procedure sp_EditarEspecialidade(in codEs int, in descrip varchar(100))
		Begin
			Update Especialidades E 
			Set E.descripcion = descrip
				Where codigoEspecialidad = codEs;
        End$$
Delimiter ;

call sp_EditarEspecialidade(1, 'Extracion de dientes');

-- ------------------------------MEDICAMENTO ------------------------------ --

-- ------------------------------AGREGAR MEDICAMENTO ------------------------------ --
Delimiter $$
	create procedure sp_AgregarMedicamento(in nombreMedicamento varchar(50))
		Begin
			Insert into Medicamento(nombreMedicamento) Values(nombreMedicamento);
        End$$
Delimiter ;

call sp_AgregarMedicamento('Acetaminofen');
call sp_AgregarMedicamento('Anestecia');

-- ------------------------------LISTAR MEDICAMENTO------------------------------ --
Delimiter $$
	create procedure sp_ListarMedicamentos()
		Begin
			select
            M.codigoMedicamento, 
            M.nombreMedicamento
            from Medicamento M;
        End$$
Delimiter ;

call sp_ListarMedicamentos();

-- ------------------------------BUSCAR MEDICAMENTO------------------------------ --
Delimiter $$
	create procedure sp_BuscarMedicamento(in codMed int)
		Begin
			select
			M.codigoMedicamento, 
            M.nombreMedicamento
            from Medicamento M where codigoMedicamento = codMed;
        End$$
Delimiter ;

call sp_BuscarMedicamento(1);

-- ------------------------------ELIMINAR MEDICAMENTO------------------------------ --
Delimiter $$
	create procedure sp_EliminarMedicamento(in codMed int)
		Begin
			delete from Medicamento
				where codigoMedicamento = codMed;
        End$$
Delimiter ;

call sp_EliminarMedicamento(2);

-- ------------------------------EDITAR MEDICAMENTO------------------------------ --
Delimiter $$
	create procedure sp_EditarMedicamento(in codMed int, in nomDes varchar(50))
		Begin
			update Medicamento M
            set nombreMedicamento = nomDes
				where codigoMedicamento = codMed;
        End$$
Delimiter ;

call sp_EditarMedicamento(1, 'Ibuprofeno 500');

-- ------------------------------DOCTORES------------------------------ --

-- ------------------------------AGREGAR DOCTORES------------------------------ --
Delimiter $$
	create procedure sp_AgregarDoctor(in numeroColegiado int, in nombresDoctores varchar(50), 
				in apellidosDoctor varchar(50), in telefonoContacto varchar(50), in codigoEspecialidad int)
		Begin
			insert into Doctores(numeroColegiado, nombresDoctores, apellidosDoctor, telefonoContacto, codigoEspecialidad)
						values(numeroColegiado, nombresDoctores, apellidosDoctor, telefonoContacto, codigoEspecialidad);
        End$$
Delimiter ;

call sp_AgregarDoctor(2, 'Eser Eliel', 'Gómez Mendez', '88131289', 1);
call sp_AgregarDoctor(5, 'Alejandro Hilario', 'Garcia Estrada', '21204002', 1);

-- ------------------------------LISTAR DOCTORES------------------------------ --
Delimiter $$
	create procedure sp_ListarDoctores()
		Begin
			select
            D.numeroColegiado, 
            D.nombresDoctores, 
            D.apellidosDoctor, 
            D.telefonoContacto, 
            D.codigoEspecialidad
            from Doctores D;
        End$$
Delimiter ;

call sp_ListarDoctores();

-- ------------------------------BUSCAR DOCTORES------------------------------ --
Delimiter $$
	create procedure numeroColegiado(in codCol int)
		Begin
			select
			D.numeroColegiado, 
            D.nombresDoctores, 
            D.apellidosDoctor, 
            D.telefonoContacto, 
            D.codigoEspecialidad
            from Doctores D where numeroColegiado = codCol;
        End$$
Delimiter ;

call numeroColegiado(2);

-- ------------------------------ELIMINAR DOCTORES------------------------------ --
Delimiter $$
	create procedure sp_EliminarDoctor(in codCol int)
		Begin
			delete from Doctores
				where numeroColegiado = codCol;
        End$$
Delimiter 

call sp_EliminarDoctor(2);

-- ------------------------------EDITAR DOCTORES------------------------------ --
Delimiter $$
	create procedure sp_EditarDoctor(in codCol int, in nomDoc varchar(50), in apeDoc varchar(50), 
									in telCont varchar(8), in codEs int)
		Begin
			update Doctores D
				set	
					D.nombresDoctores = nomDoc, 
                    D.apellidosDoctor = apeDoc, 
                    D.telefonoContacto = telCont, 
                    D.codigoEspecialidad = codEs
                    where numeroColegiado = codCol;
        End$$
Delimiter ;

call sp_EditarDoctor(2, 'Eser Eliel', 'Perez Juarez', '30261598', 1);

-- ------------------------------RECETAS------------------------------ --

-- ------------------------------AGREGAR RECETAS------------------------------ --
Delimiter $$
	create procedure sp_AgregarReceta(in fechaReceta date, in numeroColegiado int)
		Begin
			insert into Recetas(fechaReceta, numeroColegiado) values(fechaReceta, numeroColegiado);
        End$$
Delimiter ;

call sp_AgregarReceta(now(), 2);
call sp_AgregarReceta(now(), 5);

-- ------------------------------LISTAR RECETAS------------------------------ --
Delimiter $$
	create procedure sp_ListarRecetas()
		begin
			select
            R.codigoReceta, 
            R.fechaReceta, 
            R.numeroColegiado
            from Recetas R;
        end$$
Delimiter ;

call sp_ListarRecetas();

-- ------------------------------BUSCAR RECETAS------------------------------ --
Delimiter $$
	create procedure sp_BuscarReceta(in codRe int)
		begin
			select
            R.codigoReceta, 
            R.fechaReceta, 
            R.numeroColegiado
            from Recetas R where codigoReceta = codRe;
        end$$
Delimiter ;

call sp_BuscarReceta(1);

-- ------------------------------ELIMINAR RECETAS------------------------------ --
Delimiter $$
	create procedure sp_EliminarReceta(in codRe int)
		begin
			delete from Recetas 
				where codigoReceta = codRe;
        end$$
Delimiter ;

call sp_EliminarReceta(2);

-- ------------------------------EDITAR RECETAS------------------------------ --
Delimiter $$
	create procedure sp_EditarReceta(in codRe int, in feRe date, in numCol int)
		begin
			update Recetas R
			set R.fechaReceta = feRe, 
				R.numeroColegiado = numCol
				where codigoReceta = codRe;
        end$$
Delimiter ;

call sp_EditarReceta(1, now(), 2);

-- ------------------------------DETALLERECETA------------------------------ --

-- ------------------------------AGREGAR DETALLERECETA------------------------------ --
Delimiter $$
	create procedure sp_AgregarDetalleReceta(in dosis varchar(100), in codigoReceta int, in codigoMedicamento int)
		begin
			insert into DetalleReceta(dosis, codigoReceta, codigoMedicamento) values(dosis, codigoReceta, codigoMedicamento);
        end$$
Delimiter ;

call sp_AgregarDetalleReceta('500gm pastilla', 1, 1);
call sp_AgregarDetalleReceta('Una inyeccion', 1, 1);

-- ------------------------------LISTAR DETALLERECETA------------------------------ --
Delimiter $$
	create procedure sp_ListarDetalleRecetas()
		Begin
			select 
            D.CodigoDetalleReceta, 
            D.dosis, 
            D.codigoReceta, 
            D.codigoMedicamento
            from DetalleReceta D;
        end$$
Delimiter ;

call sp_ListarDetalleRecetas();

-- ------------------------------BUSCAR DETALLERECETA------------------------------ --
Delimiter $$
	create procedure sp_BuscarDetalleReceta(in codDe int)
		Begin
			select 
            D.CodigoDetalleReceta, 
            D.dosis, 
            D.codigoReceta, 
            D.codigoMedicamento
            from DetalleReceta D where CodigoDetalleReceta = codDe;
        end$$
Delimiter ;

call sp_BuscarDetalleReceta(1);

-- -----------------------------ELIMINAR DETALLERECETA------------------------------ --
Delimiter $$
	create procedure sp_EliminarDetalleReceta(in codDe int)
		begin
			delete from DetalleReceta
				where CodigoDetalleReceta = codDe;
        end$$
Delimiter ;

call sp_EliminarDetalleReceta(2);

-- -----------------------------EDITAR DETALLERECETA------------------------------ --
Delimiter $$
	create procedure sp_EditarDetalleReceta(in codDe int, in dosi varchar(100))
		begin
			update DetalleReceta D
				set D.dosis = dosi 
                where CodigoDetalleReceta = codDe;
        end$$
Delimiter ;

call sp_EditarDetalleReceta(1, '600gm pastilla');

-- ------------------------------------------CITAS-------------------------------------------------- --
-- ------------------------------------------AGREGAR CITAS------------------------------------------- --
Delimiter $$
create procedure sp_agregarCitas (in fechaCita date, in horaCita time, in tratamiento varchar(500),
	in descripcionCondActual varchar(500), in codigoPaciente int, in numeroColegiado int)
		begin
			Insert into Citas (fechaCita, horaCita, tratamiento, descripcionCondActual, codigoPaciente, numeroColegiado)
			Values (fechaCita, horaCita, tratamiento, descripcionCondActual, codigoPaciente, numeroColegiado);
		End$$
Delimiter ;

call sp_agregarCitas(now(), '11:12:10', 'Limpieza bocal', 'Caries en los dientes', 2, 2);
call sp_agregarCitas(now(), '15:09:10', 'Dientes sencibles', 'Limpieza', 2, 2);
call sp_agregarCitas(now(), '22:10:10', 'Quitar dientes', 'Dientes de leche', 2, 2);

-- ------------------------------------------LISTAR CITAS------------------------------------------- --
Delimiter $$
create procedure sp_listarCitas()
	Begin
		Select
			c.codigoCita,
			c.fechaCita,
			c.horaCita,
			c.tratamiento,
			c.descripcionCondActual,
			c.codigoPaciente,
			c.numeroColegiado
		from Citas C;
	End$$
Delimiter ;

Call sp_listarCitas();

-- ------------------------------------------BUSCAR CITAS------------------------------------------- --
Delimiter $$
Create procedure sp_buscarCitas(in codCita int)
		Begin
			Select
				c.codigoCita,
				c.fechaCita,
				c.horaCita,
				c.tratamiento,
				c.descripcionCondActual,
				c.codigoPaciente,
				c.numeroColegiado
			From Citas C
		where codigoCita = codCita;
	End$$
Delimiter ;

call sp_buscarCitas(2);

-- ------------------------------------------ELIMINAR CITAS----------------------------------------- --
Delimiter $$
Create procedure sp_eliminarCitas(in codCit int)
	Begin
		Delete from Citas
		Where codigoCita = codCit;
	End$$
Delimiter ;

call sp_listarCitas();

-- ------------------------------------------EDITAR CITAS------------------------------------------- --
Delimiter $$
Create procedure sp_editarCitas (in codCit int, in fechCit date, in horCita time, in trat varchar(500),
	in descripCondActual varchar(500), in codPacient int, in numColegia int)
		Begin
		Update Citas C
			set
				C.fechaCita = fechCit,
				C.horaCita = horCita,
				C.tratamiento = trat,
				C.descripcionCondActual = descripCondActual,
				C.codigoPaciente = codPacient,
				C.numeroColegiado = numColegia
		Where codigoCita = codCit;
	End$$
Delimiter ;

Call sp_editarCitas(1, now(), '10:59:00', 'LLeneado de diente', 'Se le callo un diente', 2, 2);

Call sp_editarCitas(1, now(), '10:59:00', 'LLeneado de diente', 'Se le callo un diente', 2, 2);


ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Admin2021';

Create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar (100) not null,
    apellidoUsuario varchar (100) not null,
    usuarioLogin varchar (50) not null,
    contrasena varchar (50) not null,
    primary key PK_codigoUsuario(codigoUsuario)
);

-- -----------------Agregar Usuario----------------- --
Delimiter $$
	Create procedure sp_AgregarUsuario(in nombreUsuario varchar (100), in apellidoUsuario varchar (100),
									   in usuarioLogin varchar (50), in contrasena varchar (50))
                                       
          Begin 
			insert into Usuario (nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
				values (nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
			End$$
Delimiter ;

-- -----------------Listar Usuario----------------- --
Delimiter $$
	Create procedure sp_ListarUsuario()
		Begin 
			Select 
				E.codigoUsuario,
                E.nombreUsuario,
                E.apellidoUsuario,
                E.usuarioLogin,
                E.contrasena
                from Usuario E;
			End$$
Delimiter ;

Call sp_AgregarUsuario('Edgar','Gómez','egomez','Admin2021');
call sp_ListarUsuario();

Create table Login(
	UsuarioMaster varchar (50) not null,
    passwordLogin varchar (50) not null,
    primary key PK_usuarioMaster (usuarioMaster)
);