import { components } from "@/types/api";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";

type UserProps = {
  user: components["schemas"]["UserResponse"];
};

export function User({ user }: UserProps) {
  const { firstName, lastName, username } = user;

  const firstInitial = !!firstName ? firstName.charAt(0).toUpperCase() : "";
  const lastInitial = !!lastName ? lastName.charAt(0).toUpperCase() : "";
  const initials = firstInitial + lastInitial;

  return (
    <div className="flex gap-x-2 items-center">
      <Avatar>
        <AvatarImage src={user.avatarSrc} />
        <AvatarFallback>{initials}</AvatarFallback>
      </Avatar>
      <div>
        <p>{username}</p>
        <p>{`${firstName} ${lastName}`}</p>
      </div>
    </div>
  );
}
