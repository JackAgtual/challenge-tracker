import { getInitials } from "@/lib/name-utils";
import { components } from "@/types/api";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";

type Props = {
  user: components["schemas"]["UserResponse"];
};

export function User({ user }: Props) {
  const { firstName, lastName, username } = user;

  return (
    <div className="flex gap-x-2 items-center">
      <Avatar>
        <AvatarImage src={user.avatarSrc} />
        <AvatarFallback>{getInitials(user)}</AvatarFallback>
      </Avatar>
      <div>
        <p>{username}</p>
        <p>{`${firstName} ${lastName}`}</p>
      </div>
    </div>
  );
}
